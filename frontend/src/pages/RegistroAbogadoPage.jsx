import React, { useState } from 'react';
import { registrarAbogado } from '../components/Fetch.jsx';
import './RegistroAbogado.css';
import { redirect } from 'react-router-dom';

function RegistroAbogado() {
  const [dni, setDni] = useState('');
  const [numeroColegiando, setNumeroColegiando] = useState('');
  const [nombre, setNombre] = useState('');
  const [telefono, setTelefono] = useState('');
  const [correo, setCorreo] = useState('');
  const [contrasena, setContrasena] = useState('');
  const [mensaje, setMensaje] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMensaje('');
    setError('');

    const abogado = {
      id: {
        dni,
        numeroColegiando
      },
      nombre,
      telefono,
      correo,
      contrasena
    };

    try {
      const resp = await registrarAbogado(abogado);
      setMensaje('Registro completado correctamente');
      setDni('');
      setNumeroColegiando('');
      setNombre('');
      setTelefono('');
      setCorreo('');
      setContrasena('');
      navigate("/");
    } catch (e) {
      setError('Error al registrar abogado. Revisa los datos.');
    }
  };

  return (
    <div className="registro-container">
      <form className="registro-form" onSubmit={handleSubmit}>
        <h2>Registro de Abogado</h2>

        <label>DNI</label>
        <input type="text" value={dni} onChange={e => setDni(e.target.value)} required />

        <label>Número de Colegiado</label>
        <input type="text" value={numeroColegiando} onChange={e => setNumeroColegiando(e.target.value)} required />

        <label>Nombre Completo</label>
        <input type="text" value={nombre} onChange={e => setNombre(e.target.value)} required />

        <label>Teléfono</label>
        <input type="text" value={telefono} onChange={e => setTelefono(e.target.value)} required />

        <label>Correo</label>
        <input type="email" value={correo} onChange={e => setCorreo(e.target.value)} required />

        <label>Contraseña</label>
        <input type="password" value={contrasena} onChange={e => setContrasena(e.target.value)} required />

        <button type="submit">Registrar</button>

        {mensaje && <div className="mensaje">{mensaje}</div>}
        {error && <div className="error">{error}</div>}
      </form>
    </div>
  );
}

export default RegistroAbogado;
