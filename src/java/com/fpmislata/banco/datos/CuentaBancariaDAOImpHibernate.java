/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpmislata.banco.datos;

import com.fpmislata.banco.modelo.CuentaBancaria;
import com.fpmislata.banco.modelo.Usuario;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author alumno
 */
public class CuentaBancariaDAOImpHibernate extends GenericDAOImpHibernate<CuentaBancaria, Integer> implements CuentaBancariaDAO {

    @Override
    public CuentaBancaria findByCodigo(String CodigoCuentaCliente) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery("SELECT cuentaBancaria FROM CuentaBancaria cuentaBancaria WHERE cuentaBancaria.sucursalBancaria.codigoSucursal=? AND cuentaBancaria.sucursalBancaria.entidadBancaria.codigoEntidadBancaria=? AND cuentaBancaria.dc=? AND cuentaBancaria.numeroCuenta=?");

        query.setString(0, CodigoCuentaCliente.substring(0, 4));
        query.setString(1, CodigoCuentaCliente.substring(4, 8));
        query.setString(2, CodigoCuentaCliente.substring(8, 10));
        query.setString(3, CodigoCuentaCliente.substring(10, 20));

        List<CuentaBancaria> listaCuentaBancaria = query.list();
        session.getTransaction().commit();

        if (listaCuentaBancaria != null) {
            if (listaCuentaBancaria.size() == 1) {
                CuentaBancaria cuentaBancaria = listaCuentaBancaria.get(0);
                return cuentaBancaria;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public List<CuentaBancaria> findByUser(int idUsuario) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("SELECT cuentaBancaria FROM CuentaBancaria cuentaBancaria WHERE cuentaBancaria.usuario=? ");
        query.setInteger(0, idUsuario);
        List<CuentaBancaria> listaCuentaBancaria = query.list();
        if (listaCuentaBancaria != null) {
            return listaCuentaBancaria;
        } else {
            return null;
        }
    }
}
