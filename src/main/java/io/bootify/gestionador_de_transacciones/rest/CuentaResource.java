package io.bootify.gestionador_de_transacciones.rest;

import io.bootify.gestionador_de_transacciones.model.CuentaDTO;
import io.bootify.gestionador_de_transacciones.service.CuentaService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/cuentas", produces = MediaType.APPLICATION_JSON_VALUE)
public class CuentaResource {

    private final CuentaService cuentaService;

    public CuentaResource(final CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public ResponseEntity<List<CuentaDTO>> getAllCuentas() {
        return ResponseEntity.ok(cuentaService.findAll());
    }

    @GetMapping("/{iban}")
    public ResponseEntity<CuentaDTO> getCuenta(@PathVariable(name = "iban") final String iban) {
        return ResponseEntity.ok(cuentaService.get(iban));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createCuenta(@RequestBody @Valid final CuentaDTO cuentaDTO,
            final BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (!bindingResult.hasFieldErrors("iban") && cuentaDTO.getIban() == null) {
            bindingResult.rejectValue("iban", "NotNull");
        }
        if (!bindingResult.hasFieldErrors("iban") && cuentaService.ibanExists(cuentaDTO.getIban())) {
            bindingResult.rejectValue("iban", "Exists.cuenta.iban");
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(new MethodParameter(
                    this.getClass().getDeclaredMethods()[0], -1), bindingResult);
        }
        final String createdIban = cuentaService.create(cuentaDTO);
        return new ResponseEntity<>(createdIban, HttpStatus.CREATED);
    }

    @PutMapping("/{iban}")
    public ResponseEntity<String> updateCuenta(@PathVariable(name = "iban") final String iban,
            @RequestBody @Valid final CuentaDTO cuentaDTO) {
        cuentaService.update(iban, cuentaDTO);
        return ResponseEntity.ok(iban);
    }

    @DeleteMapping("/{iban}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCuenta(@PathVariable(name = "iban") final String iban) {
        cuentaService.delete(iban);
        return ResponseEntity.noContent().build();
    }

}
