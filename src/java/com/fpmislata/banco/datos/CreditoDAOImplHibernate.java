/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpmislata.banco.datos;

import com.fpmislata.banco.modelo.Credito;
import com.fpmislata.banco.modelo.CuentaBancaria;
import com.fpmislata.banco.modelo.Usuario;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Ivan
 */
public class CreditoDAOImplHibernate extends GenericDAOImpHibernate<Credito, Integer> implements CreditoDAO {

    @Override
    public boolean comprobarCredito(Usuario usuario) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        CuentaBancariaDAO cuentaBancariaDAO=new CuentaBancariaDAOImpHibernate();
        /*Query query = session.createQuery("SELECT cuentaBancaria FROM CuentaBancaria cuentaBancaria WHERE IdUsuario=?");
        query.setInteger(0, usuario.getIdUsuario());*/
        
        List<CuentaBancaria> listaCuentas = cuentaBancariaDAO.findByUser(usuario.getIdUsuario());

        if (listaCuentas.isEmpty()){//si la lista de cuentas esta vacia devuelve falso
            return false;
        } else {//si esta llena
            for (int i = 0; i <= listaCuentas.size(); i++) {//recorremos la lista de cuentas
                CuentaBancaria cuentaBancaria = listaCuentas.get(i);//recoge el valor i de la lista
                if (cuentaBancaria.getSaldo() < 0) {//comprueba si el saldo es menor a 0
                    return false; // devuelve false
                }//cierre if
            }//cierre for
            return true;
        }//cierre else
        
    }//cierre comprobarCredito
}
