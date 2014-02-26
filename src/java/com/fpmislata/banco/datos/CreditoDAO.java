/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fpmislata.banco.datos;

import com.fpmislata.banco.modelo.Credito;
import com.fpmislata.banco.modelo.Usuario;

/**
 *
 * @author Ivan
 */
public interface CreditoDAO extends GenericDAO<Credito,Integer>{
    public boolean comprobarCredito(Usuario usuario);
}
