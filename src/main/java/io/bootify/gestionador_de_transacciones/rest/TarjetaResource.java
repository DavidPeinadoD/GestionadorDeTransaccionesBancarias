package io.bootify.gestionador_de_transacciones.rest;

import io.bootify.gestionador_de_transacciones.model.TarjetaDTO;
import io.bootify.gestionador_de_transacciones.service.TarjetaService;
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
@RequestMapping(value = "/api/tarjetas", produces = MediaType.APPLICATION_JSON_VALUE)
public class TarjetaResource {

    private final TarjetaService tarjetaService;

    public TarjetaResource(final TarjetaService tarjetaService) {
        this.tarjetaService = tarjetaService;
    }

    @GetMapping
    public ResponseEntity<List<TarjetaDTO>> getAllTarjetas() {
        return ResponseEntity.ok(tarjetaService.findAll());
    }

    @GetMapping("/{iban}")
    public ResponseEntity<TarjetaDTO> getTarjeta(@PathVariable(name = "iban") final String iban) {
        return ResponseEntity.ok(tarjetaService.get(iban));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<String> createTarjeta(@RequestBody @Valid final TarjetaDTO tarjetaDTO,
            final BindingResult bindingResult) throws MethodArgumentNotValidException {
        if (!bindingResult.hasFieldErrors("iban") && tarjetaDTO.getIban() == null) {
            bindingResult.rejectValue("iban", "NotNull");
        }
        if (!bindingResult.hasFieldErrors("iban") && tarjetaService.ibanExists(tarjetaDTO.getIban())) {
            bindingResult.rejectValue("iban", "Exists.tarjeta.iban");
        }
        if (bindingResult.hasErrors()) {
            throw new MethodArgumentNotValidException(new MethodParameter(
                    this.getClass().getDeclaredMethods()[0], -1), bindingResult);
        }
        final String createdIban = tarjetaService.create(tarjetaDTO);
        return new ResponseEntity<>(createdIban, HttpStatus.CREATED);
    }

    @PutMapping("/{iban}")
    public ResponseEntity<String> updateTarjeta(@PathVariable(name = "iban") final String iban,
            @RequestBody @Valid final TarjetaDTO tarjetaDTO) {
        tarjetaService.update(iban, tarjetaDTO);
        return ResponseEntity.ok(iban);
    }

    @DeleteMapping("/{iban}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTarjeta(@PathVariable(name = "iban") final String iban) {
        tarjetaService.delete(iban);
        return ResponseEntity.noContent().build();
    }

}
