package io.bootify.gestionador_de_transacciones.controller;

import io.bootify.gestionador_de_transacciones.domain.Cuenta;
import io.bootify.gestionador_de_transacciones.model.TarjetaDTO;
import io.bootify.gestionador_de_transacciones.repos.CuentaRepository;
import io.bootify.gestionador_de_transacciones.service.TarjetaService;
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
@RequestMapping("/tarjetas")
public class TarjetaController {

    private final TarjetaService tarjetaService;
    private final CuentaRepository cuentaRepository;

    public TarjetaController(final TarjetaService tarjetaService,
            final CuentaRepository cuentaRepository) {
        this.tarjetaService = tarjetaService;
        this.cuentaRepository = cuentaRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("tarjetaCuentaValues", cuentaRepository.findAll(Sort.by("iban"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Cuenta::getIban, Cuenta::getIban)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("tarjetas", tarjetaService.findAll());
        return "tarjeta/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("tarjeta") final TarjetaDTO tarjetaDTO) {
        return "tarjeta/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("tarjeta") @Valid final TarjetaDTO tarjetaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("iban") && tarjetaDTO.getIban() == null) {
            bindingResult.rejectValue("iban", "NotNull");
        }
        if (!bindingResult.hasFieldErrors("iban") && tarjetaService.ibanExists(tarjetaDTO.getIban())) {
            bindingResult.rejectValue("iban", "Exists.tarjeta.iban");
        }
        if (!bindingResult.hasFieldErrors("numeroPAN") && tarjetaService.numeroPANExists(tarjetaDTO.getNumeroPAN())) {
            bindingResult.rejectValue("numeroPAN", "Exists.tarjeta.numeroPAN");
        }
        if (bindingResult.hasErrors()) {
            return "tarjeta/add";
        }
        tarjetaService.create(tarjetaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("tarjeta.create.success"));
        return "redirect:/tarjetas";
    }

    @GetMapping("/edit/{iban}")
    public String edit(@PathVariable(name = "iban") final String iban, final Model model) {
        model.addAttribute("tarjeta", tarjetaService.get(iban));
        return "tarjeta/edit";
    }

    @PostMapping("/edit/{iban}")
    public String edit(@PathVariable(name = "iban") final String iban,
            @ModelAttribute("tarjeta") @Valid final TarjetaDTO tarjetaDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final TarjetaDTO currentTarjetaDTO = tarjetaService.get(iban);
        if (!bindingResult.hasFieldErrors("numeroPAN") &&
                !tarjetaDTO.getNumeroPAN().equals(currentTarjetaDTO.getNumeroPAN()) &&
                tarjetaService.numeroPANExists(tarjetaDTO.getNumeroPAN())) {
            bindingResult.rejectValue("numeroPAN", "Exists.tarjeta.numeroPAN");
        }
        if (bindingResult.hasErrors()) {
            return "tarjeta/edit";
        }
        tarjetaService.update(iban, tarjetaDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("tarjeta.update.success"));
        return "redirect:/tarjetas";
    }

    @PostMapping("/delete/{iban}")
    public String delete(@PathVariable(name = "iban") final String iban,
            final RedirectAttributes redirectAttributes) {
        tarjetaService.delete(iban);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("tarjeta.delete.success"));
        return "redirect:/tarjetas";
    }

}
