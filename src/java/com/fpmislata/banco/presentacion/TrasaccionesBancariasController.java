/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpmislata.banco.presentacion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fpmislata.banco.datos.CuentaBancariaDAO;
import com.fpmislata.banco.datos.MovimientoBancarioDAO;
import com.fpmislata.banco.datos.MovimientoBancarioDAOImpHibernate;
import com.fpmislata.banco.modelo.BussinesMessage;
import com.fpmislata.banco.modelo.CuentaBancaria;
import com.fpmislata.banco.modelo.MovimientoBancario;
import com.fpmislata.banco.modelo.TipoMovimientoBancario;
import com.fpmislata.banco.modelo.TransaccionBancaria;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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
public class TrasaccionesBancariasController {//controlador de transacciones Bancarias
//recibe un json con los datos y crea dos movimientos---Deber y Haber
    @Autowired//inyeccion de dependencias---Existe una bean en application context
    private CuentaBancariaDAO cuentaBancariaDAO;//primer objeto con el que vamos a trabajar
    
    @Autowired//inyeccion de dependencias
    private MovimientoBancarioDAO movimientoBancarioDAO;//segundo objeto con el vamos a trabajar
    
   

@RequestMapping(value = {"/TransaccionBancaria"}, method = RequestMethod.POST)//en caso de recibir una peticion por post a /TransaccionBancaria realiza un insert
    public void insert(HttpServletRequest httpRequest, HttpServletResponse httpServletResponse, @RequestBody String json) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();//objeto que permite mapear los JSON
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);//configura el objeto, para que escriba como fechas los timestamps
            TransaccionBancaria transaccionBancaria = (TransaccionBancaria) objectMapper.readValue(json, TransaccionBancaria.class);
            //Crea un objeto transaccionBancaria con el objeto casteado y mapeado por el ObjectoMapper.readValue
            //readValue(json, TransaccionBancaria.class) lee el json, y lo mapea con la clase Transaccion Bancaria
            
            
            
            CuentaBancaria cuentaBancariaOrigen = cuentaBancariaDAO.findByCodigo(transaccionBancaria.getCodigoCuentaClienteOrigen());//guarda la cuenta origen
            //transaccionBancaria.getCodigoCuentaClienteOrigen() coge la cuenta cliente de la transaccion mapeada
            //cuentaBancariaDAO.findByCodigo(anterior) busca la cuenta bancaria por el codigo
            CuentaBancaria cuentaBancariaDestino = cuentaBancariaDAO.findByCodigo(transaccionBancaria.getCodigoCuentaClienteDestino());
           //igual que la de antes pero con la de destino
            /*-----------------------Origen---------------------------*/  //Creamos el movimiento para la cuenta origen
            MovimientoBancario movimientoBancarioOrigen = new MovimientoBancario();//nuevo movimiento bancario
            
            movimientoBancarioOrigen.setCuentaBancaria(cuentaBancariaOrigen);//guarda la cuenta bancaria origen
            movimientoBancarioOrigen.setTipoMovimientoBancario(TipoMovimientoBancario.Debe);//pone el tipo a deber
            movimientoBancarioOrigen.setImporte(transaccionBancaria.getTotal());//recoge y guarda el valor de la transaccion en el movimiento
            movimientoBancarioOrigen.setFecha(new Date());//graba la fecha actual
            movimientoBancarioOrigen.setConcepto(transaccionBancaria.getConcepto());//graba el concepto de la transaccion en el movimiento
            
            movimientoBancarioDAO.insert(movimientoBancarioOrigen);//realiza el insert del movimiento origen
            
            /*-----------------------Destino---------------------------*/
            MovimientoBancario movimientoBancarioDestino = new MovimientoBancario();
            
            movimientoBancarioDestino.setCuentaBancaria(cuentaBancariaDestino);
            movimientoBancarioDestino.setTipoMovimientoBancario(TipoMovimientoBancario.Haber);
            movimientoBancarioDestino.setImporte(transaccionBancaria.getTotal());
            movimientoBancarioDestino.setFecha(new Date());
            movimientoBancarioDestino.setConcepto(transaccionBancaria.getConcepto());
            
            movimientoBancarioDAO.insert(movimientoBancarioDestino);
            
            noCache(httpServletResponse);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);//si todo va bien devuelve un OK
        } catch (ConstraintViolationException cve) {
            List<BussinesMessage> errorList = new ArrayList();
            ObjectMapper jackson = new ObjectMapper();
            System.out.println("No se ha podido insertar el movimiento bancario debido a los siguientes errores:");
            for (ConstraintViolation constraintViolation : cve.getConstraintViolations()) {
               String datos = constraintViolation.getPropertyPath().toString();
               String mensage = constraintViolation.getMessage();
               
               BussinesMessage bussinesMessage = new BussinesMessage(datos,mensage);
               errorList.add(bussinesMessage);
            }
            String jsonInsert = jackson.writeValueAsString(errorList);
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
private void noCache(HttpServletResponse httpServletResponse){
      httpServletResponse.setHeader("Cache-Control", "no-cache");//permite que no cachee el servidor
  }
}
