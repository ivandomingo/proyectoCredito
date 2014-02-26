/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpmislata.banco.presentacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fpmislata.banco.datos.CreditoDAO;
import com.fpmislata.banco.datos.CuentaBancariaDAO;
import com.fpmislata.banco.datos.MovimientoBancarioDAO;
import com.fpmislata.banco.modelo.Credito;
import com.fpmislata.banco.modelo.CuentaBancaria;
import com.fpmislata.banco.modelo.MovimientoBancario;
import com.fpmislata.banco.modelo.TipoMovimientoBancario;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Ivan
 */
public class CreditoController {

    @Autowired
    private CreditoDAO creditoBancarioDAO;
    @Autowired
    private MovimientoBancarioDAO movimientoBancarioDAO;
    @Autowired
    private CuentaBancariaDAO cuentaBancariaDAO;

    @RequestMapping(value = {"/Credito"}, method = RequestMethod.POST)
    public void insert(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse, @RequestBody String json) throws IOException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        Credito credito = (Credito) objectMapper.readValue(json, Credito.class);
        
        if (creditoBancarioDAO.comprobarCredito(credito.getUsuario())) {
            MovimientoBancario movimientoBancario = new MovimientoBancario();//nuevo movimiento bancario
            List<CuentaBancaria> listaCuentasCliente = cuentaBancariaDAO.findByUser(credito.getUsuario().getIdUsuario());

            for (int i = 0; i <= listaCuentasCliente.size();) {
                CuentaBancaria primeraCuenta=cuentaBancariaDAO.findByCodigo(listaCuentasCliente.get(i).getNumeroCuenta());
                
                movimientoBancario.setCuentaBancaria(primeraCuenta);//guarda la cuenta bancaria origen
                movimientoBancario.setTipoMovimientoBancario(TipoMovimientoBancario.Haber);//pone el tipo a deber
                movimientoBancario.setImporte(credito.getTotalCredito());//recoge y guarda el valor de la transaccion en el movimiento
                movimientoBancario.setFecha(credito.getFechaExpedicion());//graba la fecha actual
                movimientoBancario.setConcepto("Credito expedido por la Entidad Bancaria");//graba el concepto de la transaccion en el movimiento
            }
            creditoBancarioDAO.insert(credito);
            movimientoBancarioDAO.insert(movimientoBancario);//realiza el insert del movimiento origen
            noCache(httpServletResponse);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);//si todo va bien devuelve un OK
        }
    }

    private void noCache(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Cache-Control", "no-cache");//permite que no cachee el servidor
    }
}
