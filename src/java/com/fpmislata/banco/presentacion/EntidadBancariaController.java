/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpmislata.banco.presentacion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fpmislata.banco.datos.EntidadBancariaDAO;
import com.fpmislata.banco.modelo.BussinesMessage;
import com.fpmislata.banco.modelo.EntidadBancaria;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author alumno
 */
@Controller
public class EntidadBancariaController {

    @Autowired
    private EntidadBancariaDAO entidadBancariaDAO;

    @RequestMapping(value = {"/EntidadBancaria/{idEntidadBancaria}"}, method = RequestMethod.GET)
    public void read(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse, @PathVariable("idEntidadBancaria") int idEntidadBancaria) {
        try {
            ObjectMapper jackson = new ObjectMapper();
            String json = jackson.writeValueAsString(entidadBancariaDAO.read(idEntidadBancaria));
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setContentType("application/json; charset=UTF-8");

            httpServletResponse.getWriter().println(json);

        } catch (Exception ex) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.setContentType("text/plain; charset=UTF-8");
            try {
                ex.printStackTrace(httpServletResponse.getWriter());
            } catch (Exception ex1) {
            }
        }


    }
    @RequestMapping(value = {"/EntidadesBancarias"}, method = RequestMethod.GET)
    public void readAll(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse) {
        try {
            ObjectMapper jackson = new ObjectMapper();
            String json = jackson.writeValueAsString(entidadBancariaDAO.findAll());
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setContentType("application/json; charset=UTF-8");

            httpServletResponse.getWriter().println(json);

        } catch (Exception ex) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.setContentType("text/plain; charset=UTF-8");
            try {
                ex.printStackTrace(httpServletResponse.getWriter());
            } catch (Exception ex1) {
            }
        }


    }

    @RequestMapping(value = {"/EntidadBancaria/{idEntidadBancaria}"}, method = RequestMethod.DELETE)
    public void delete(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse, @PathVariable("idEntidadBancaria") int idEntidadBancaria) {
        try {
            entidadBancariaDAO.delete(idEntidadBancaria);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.setContentType("text/plain; charset=UTF-8");
            try {
                ex.printStackTrace(httpServletResponse.getWriter());
            } catch (Exception ex1) {
            }
        }
    }

    @RequestMapping(value = {"/EntidadBancaria/"}, method = RequestMethod.POST)
    public void insert(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse, @RequestBody String json) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            EntidadBancaria entidadBancaria = (EntidadBancaria) objectMapper.readValue(json, EntidadBancaria.class);
            entidadBancariaDAO.insert(entidadBancaria);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        } catch (ConstraintViolationException cve) {
            List<BussinesMessage> errorList = new ArrayList();
            ObjectMapper jackson = new ObjectMapper();
            System.out.println("No se ha podido insertar la Entidad Bancaria debido a los siguientes errores:");
            for (ConstraintViolation constraintViolation : cve.getConstraintViolations()) {
               String datos = constraintViolation.getPropertyPath().toString();
               String mensage = constraintViolation.getMessage();
               
               BussinesMessage bussinesMessage = new BussinesMessage(datos,mensage);
               errorList.add(bussinesMessage);
            }
            String jsonInsert = jackson.writeValueAsString(errorList);
            httpServletResponse.setStatus(httpServletResponse.SC_BAD_REQUEST);
        } catch (Exception ex) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.setContentType("text/plain; charset=UTF-8");
            try {
                ex.printStackTrace(httpServletResponse.getWriter());
            } catch (Exception ex1) {
            }
        }
    }

    @RequestMapping(value = {"/EntidadBancaria/{idEntidadBancaria}"}, method = RequestMethod.PUT)
    public void update(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse, @PathVariable("idEntidadBancaria") int idEntidadBancaria, @RequestBody String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            EntidadBancaria entidadBancariaUpdate = entidadBancariaDAO.read(idEntidadBancaria);
            EntidadBancaria entidadBancaria = (EntidadBancaria) objectMapper.readValue(json, EntidadBancaria.class);

            entidadBancariaUpdate.setNombre(entidadBancaria.getNombre());
            entidadBancariaUpdate.setCodigoEntidadBancaria(entidadBancaria.getCodigoEntidadBancaria());
            entidadBancariaUpdate.setCif(entidadBancaria.getCif());
            entidadBancariaUpdate.setTipo(entidadBancaria.getTipo());
            entidadBancariaUpdate.setListaSucursalBancaria(entidadBancaria.getListaSucursalBancaria());

            entidadBancariaDAO.update(entidadBancariaUpdate);

            httpServletResponse.setStatus(HttpServletResponse.SC_OK);

            httpServletResponse.setContentType("application/json; charset=UTF-8");
            httpServletResponse.getWriter().println(json);
        } catch (Exception ex) {
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.setContentType("text/plain; charset=UTF-8");
            try {
                ex.printStackTrace(httpServletResponse.getWriter());
            } catch (Exception ex1) {
            }
        }
    }
}