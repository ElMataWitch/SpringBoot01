package mx.gob.queretaro.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mx.gob.queretaro.exception.InternalException;
import mx.gob.queretaro.model.Country;
import mx.gob.queretaro.request.CountryRequest;
import mx.gob.queretaro.service.ICountryService;

@RestController
@RequestMapping("api/pais")
public class CountryRest {

	private final ICountryService countryService;

	@Autowired
	public CountryRest(ICountryService countryService) {
		this.countryService = countryService;
	}

	@GetMapping(path = "obtenerTodos", produces = MediaType.APPLICATION_JSON_VALUE) // api/pais/obtenerTodos
	public Map<String, Object> obtenerTodos() {
		Map<String, Object> resultado = new HashMap<>();

		try {
			resultado.put("estado", "exito");
			resultado.put("datos", countryService.obtenerTodosOrdenadosPorId());
		} catch (InternalException ex) {
			resultado.put("estado", "error");
			resultado.put("datos", ex.getMessage());
		}

		return resultado;
	}


	@GetMapping(path = "obtenerPorIdYPais/{id}/{country}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> obtenerPorIdYPais(@PathVariable("id") Short id, @PathVariable("country") String country) {
		Map<String, Object> resultado = new HashMap<>();

		try {
			resultado.put("estado", "exito");
			resultado.put("datos", countryService.obtenerPorIdYPais(id, country));
		} catch (InternalException ex) {
			resultado.put("estado", "error");
			resultado.put("datos", ex.getMessage());
		}

		return resultado;
	}

	@GetMapping(path = "obtenerPorId/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> obtenerPorId(@PathVariable("id") Short id) {
		Map<String, Object> resultado = new HashMap<>();

		try {
			resultado.put("estado", "exito");
			resultado.put("datos", countryService.obtenerPorId(id));
		} catch (InternalException ex) {
			resultado.put("estado", "error");
			resultado.put("datos", ex.getMessage());
		}

		return resultado;
	}

	@GetMapping(path = "obtenerPaginacion", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> obtenerPaginacion(
			@RequestParam(value = "limit") int limit,
			@RequestParam(value = "offset") int offset,
			@RequestParam(value = "order") String order,
			@RequestParam(value = "sort") String sort,
			@RequestParam(value = "search", required = false, defaultValue = "") String search
			) {
		Page<Country> countries;
		Map<String, Object> paginacion = new HashMap<>();

		try {
			countries = countryService.obtenerPaginacion(limit, offset, order, sort, search);

			paginacion.put("total", (countries != null) ? countries.getTotalElements() : 0L); // Operador condicional ternario
			paginacion.put("rows", (countries != null) ? countries.getContent() : new ArrayList<>()); // Operador condicional ternario

			return new ResponseEntity<>(paginacion, HttpStatus.OK);
		} catch (InternalException ex) {
			return new ResponseEntity<>(paginacion, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(path = "obtenerSuma", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> obtenerSuma() {
		Map<String, Object> resultado = new HashMap<>();

		try {
			resultado.put("estado", "exito");
			resultado.put("datos", countryService.obtenerSuma());
		} catch (InternalException ex) {
			resultado.put("estado", "error");
			resultado.put("datos", ex.getMessage());
		}

		return resultado;
	}

	@GetMapping(path = "obtenerPaisCiudadPorIdPaisYIdCiudad/{countryId}/{cityId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> obtenerPaisCiudadPorIdPaisYIdCiudad(
			@PathVariable("countryId") Short countryId,
			@PathVariable("cityId") Short cityId) {
		Map<String, Object> resultado = new HashMap<>();

		try {
			resultado.put("estado", "exito");
			resultado.put("datos", countryService.obtenerPaisCiudadPorIdPaisYIdCiudad(countryId, cityId));
		} catch (InternalException ex) {
			resultado.put("estado", "error");
			resultado.put("datos", ex.getMessage());
		}

		return resultado;
	}


	@GetMapping(path = "obtenerNombrePaisPorPais/{country}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> obtenerNombrePaisPorPais(
			@PathVariable("country") String country) {
		Map<String, Object> resultado = new HashMap<>();

		try {
			resultado.put("estado", "exito");
			resultado.put("datos", countryService.obtenerNombrePaisPorPais(country));
		} catch (InternalException ex) {
			resultado.put("estado", "error");
			resultado.put("datos", ex.getMessage());
		}

		return resultado;
	}

	@PostMapping(path = "/guardar", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> guardar(@Valid @RequestBody CountryRequest countryRequest, BindingResult errores) {
		Map<String, Object> resultado = new HashMap<>();

		try {
			if (!errores.hasErrors()) {
				resultado.put("estado", "exito");
				resultado.put("datos", countryService.guardar(countryRequest));
			} else {
				List<String> mensaje = new ArrayList<>();

				for (FieldError error : errores.getFieldErrors()) {
					String campo = error.getField().trim() + " "
							+ error.getDefaultMessage().trim().replace("null", "nulo") + ".";

					mensaje.add(campo);
				}

				resultado.put("estado", "error");
				resultado.put("datos", mensaje);
			}
		} catch (InternalException ex) {
			resultado.put("estado", "error");
			resultado.put("datos", ex.getMessage());
		}

		return resultado;
	}

	@PutMapping(path = "/actualizar/{countryId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> actualizar(
			@Valid @RequestBody CountryRequest countryRequest,
			@PathVariable("countryId") Short countryId,
			BindingResult errores) {
		Map<String, Object> resultado = new HashMap<>();

		try {
			if (!errores.hasErrors()) {
				resultado.put("estado", "exito");
				resultado.put("datos", countryService.actualizar(countryRequest, countryId));
			} else {
				List<String> mensaje = new ArrayList<>();

				errores.getFieldErrors().forEach(error ->
				mensaje.add(error.getField().trim() + " "
						+ error.getDefaultMessage().trim().replace("null", "nulo") + ".")
						);

				resultado.put("estado", "error");
				resultado.put("datos", mensaje);
			}
		} catch (InternalException ex) {
			resultado.put("estado", "error");
			resultado.put("datos", ex.getMessage());
		}

		return resultado;
	}

	@PutMapping(path = "actualizarPais/{id}/{country}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> actualizarPais(
			@PathVariable("id") Short id,
			@PathVariable("country") String country
			){
		Map<String, Object> resultado = new HashMap<>();

		try {
			resultado.put("estado", "exito");
			resultado.put("datos", countryService.actualizarPais(id, country));
		} catch (InternalException ex) {
			resultado.put("estado", "error");
			resultado.put("datos", ex.getMessage());
		}

		return resultado;
	}

	@DeleteMapping(path = "borrar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> borrar(
			@PathVariable("id") Short id
			){
		Map<String, Object> resultado = new HashMap<>();

		try {
			countryService.borrar(id);

			resultado.put("estado", "exito");
			resultado.put("datos", "Se borro con exito el id: " + id);
		} catch (InternalException ex) {
			resultado.put("estado", "error");
			resultado.put("datos", ex.getMessage());
		}

		return resultado;
	}
}
