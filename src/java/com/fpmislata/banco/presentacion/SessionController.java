/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpmislata.banco.presentacion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fpmislata.banco.datos.UsuarioDAO;
import com.fpmislata.banco.modelo.BussinesMessage;
import com.fpmislata.banco.modelo.CredencialesUsuario;
import com.fpmislata.banco.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.metamodel.SetAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author alumno
 */
@Controller
public class SessionController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @RequestMapping(value = {"/Session"}, method = RequestMethod.POST)
    public void login(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse, @RequestBody String json) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            CredencialesUsuario login = (CredencialesUsuario) objectMapper.readValue(json, CredencialesUsuario.class);
            Usuario ok = usuarioDAO.readByUsername(login.getUsername());
            if (ok != null) {
                if (ok.checkPassword(login.getPassword())) {
                    HttpSession httpSession = httpRequest.getSession();
                    httpSession.setAttribute("usuario", ok.getUsername());
                    noCache(httpServletResponse);
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                } else {
                    noCache(httpServletResponse);
                    httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                }
            } else {
                noCache(httpServletResponse);
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        } catch (ConstraintViolationException cve) {
            List<BussinesMessage> errorList = new ArrayList();
            ObjectMapper jackson = new ObjectMapper();
            System.out.println("No se ha podido logear:");
            for (ConstraintViolation constraintViolation : cve.getConstraintViolations()) {
                String datos = constraintViolation.getPropertyPath().toString();
                String mensage = constraintViolation.getMessage();
                BussinesMessage bussinesMessage = new BussinesMessage(datos, mensage);
                errorList.add(bussinesMessage);
            }
            jackson.writeValueAsString(errorList);
            noCache(httpServletResponse);
            httpServletResponse.setStatus(httpServletResponse.SC_BAD_REQUEST);
        } catch (Exception ex) {
            noCache(httpServletResponse);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.setContentType("text/plain; charset=UTF-8");
            try {
                noCache(httpServletResponse);
                ex.printStackTrace(httpServletResponse.getWriter());
            } catch (Exception ex1) {
                noCache(httpServletResponse);
            }
        }
    }

    private void noCache(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Cache-Control", "no-cache");
    }
}