package com.rawrstudio.fademac.models;

/**
 * Created by Tonatiuh on 25/05/2017.
 */

public class Jugador {
    private String primerNombre, segundoNombre, apellidoPaterno, apellidoMaterno, fechaNacimiento, genero,
    equipoActual, equipoProcedencia, conExperiencia, curp, posicion, edad, peso, estatura, jersey, categoria;

    public  Jugador(){

    }

    public Jugador(String primerNombre, String segundoNombre, String apellidoPaterno, String apellidoMaterno, String fechaNacimiento, String genero, String equipoActual, String equipoProcedencia, String conExperiencia, String curp, String posicion, String edad, String peso, String estatura, String jersey, String categoria) {
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.equipoActual = equipoActual;
        this.equipoProcedencia = equipoProcedencia;
        this.conExperiencia = conExperiencia;
        this.curp = curp;
        this.posicion = posicion;
        this.edad = edad;
        this.peso = peso;
        this.estatura = estatura;
        this.jersey = jersey;
        this.categoria = categoria;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEquipoActual() {
        return equipoActual;
    }

    public void setEquipoActual(String equipoActual) {
        this.equipoActual = equipoActual;
    }

    public String getEquipoProcedencia() {
        return equipoProcedencia;
    }

    public void setEquipoProcedencia(String equipoProcedencia) {
        this.equipoProcedencia = equipoProcedencia;
    }

    public String getConExperiencia() {
        return conExperiencia;
    }

    public void setConExperiencia(String conExperiencia) {
        this.conExperiencia = conExperiencia;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getEstatura() {
        return estatura;
    }

    public void setEstatura(String estatura) {
        this.estatura = estatura;
    }

    public String getJersey() {
        return jersey;
    }

    public void setJersey(String jersey) {
        this.jersey = jersey;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
