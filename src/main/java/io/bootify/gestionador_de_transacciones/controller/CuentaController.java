package io.bootify.gestionador_de_transacciones.controller;

import io.bootify.gestionador_de_transacciones.domain.Usuario;
import io.bootify.gestionador_de_transacciones.model.CuentaDTO;
import io.bootify.gestionador_de_transacciones.repos.UsuarioRepository;
import io.bootify.gestionador_de_transacciones.service.CuentaService;
import io.bootify.gestionador_de_transacciones.util.CustomCollectors;
import io.bootify.gestionador_de_transacciones.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;
    private final UsuarioRepository usuarioRepository;

    public CuentaController(final CuentaService cuentaService,
            final UsuarioRepository usuarioRepository) {
        this.cuentaService = cuentaService;
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("cuentaUsuarioValues", usuarioRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Usuario::getId, Usuario::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("cuentas", cuentaService.findAll());
        return "cuenta/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("cuenta") final CuentaDTO cuentaDTO) {
        return "cuenta/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("cuenta") @Valid final CuentaDTO cuentaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("iban") && cuentaDTO.getIban() == null) {
            bindingResult.rejectValue("iban", "NotNull");
        }
        if (!bindingResult.hasFieldErrors("iban") && cuentaService.ibanExists(cuentaDTO.getIban())) {
            bindingResult.rejectValue("iban", "Exists.cuenta.iban");
        }
        if (!bindingResult.hasFieldErrors("titular") && cuentaService.titularExists(cuentaDTO.getTitular())) {
            bindingResult.rejectValue("titular", "Exists.cuenta.titular");
        }
        if (bindingResult.hasErrors()) {
            return "cuenta/add";
        }
        cuentaService.create(cuentaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cuenta.create.success"));
        return "redirect:/cuentas";
    }

    @GetMapping("/edit/{iban}")
    public String edit(@PathVariable(name = "iban") final String iban, final Model model) {
        model.addAttribute("cuenta", cuentaService.get(iban));
        return "cuenta/edit";
    }

    @PostMapping("/edit/{iban}")
    public String edit(@PathVariable(name = "iban") final String iban,
            @ModelAttribute("cuenta") @Valid final CuentaDTO cuentaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final CuentaDTO currentCuentaDTO = cuentaService.get(iban);
        if (!bindingResult.hasFieldErrors("titular") &&
                !cuentaDTO.getTitular().equalsIgnoreCase(currentCuentaDTO.getTitular()) &&
                cuentaService.titularExists(cuentaDTO.getTitular())) {
            bindingResult.rejectValue("titular", "Exists.cuenta.titular");
        }
        if (bindingResult.hasErrors()) {
            return "cuenta/edit";
        }
        cuentaService.update(iban, cuentaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("cuenta.update.success"));
        return "redirect:/cuentas";
    }

    @PostMapping("/delete/{iban}")
    public String delete(@PathVariable(name = "iban") final String iban,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = cuentaService.getReferencedWarning(iban);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            cuentaService.delete(iban);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("cuenta.delete.success"));
        }
        return "redirect:/cuentas";
    }

}
