import React, { useState } from 'react';
import { registrarCliente } from '../components/Fetch.jsx';
import './RegistroClienteStyle.css';

function RegistroCliente() {
  const [nombre, setNombre] = useState('');
  const [dni, setDni] = useState('');
  const [correo, setCorreo] = useState('');
  const [contrasena, setContrasena] = useState(''); 
  const [mensaje, setMensaje] = useState('');
  const [error, setError] = useState('');

  const token = new URLSearchParams(window.location.search).get('token');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMensaje('');
    setError('');

    const cliente = { nombre, dni, correo, contrasena };

    try {
      const result = await registrarCliente(cliente, token);
      setMensaje(`Cliente registrado correctamente: ${result.nombre}`);
      console.log(cliente);
      setNombre('');
      setDni('');
      setCorreo('');
      setContrasena(''); 
      navigate("/");
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="registro-container">
      <h2>Registro de Cliente</h2>

      {mensaje && <div className="mensaje">{mensaje}</div>}
      {error && <div className="error-message">{error}</div>}

      <form onSubmit={handleSubmit} className="registro-form">
        <label>
          Nombre:
          <input
            type="text"
            value={nombre}
            onChange={(e) => setNombre(e.target.value)}
            required
          />
        </label>
        <label>
          DNI:
          <input
            type="text"
            value={dni}
            onChange={(e) => setDni(e.target.value)}
            required
          />
        </label>
        <label>
          Correo:
          <input
            type="email"
            value={correo}
            onChange={(e) => setCorreo(e.target.value)}
            required
          />
        </label>
        <label>
          Contraseña:
          <input
            type="password"
            value={contrasena}
            onChange={(e) => setContrasena(e.target.value)}
            required
            minLength={6}
          />
        </label>
        <button type="submit">Registrar</button>
      </form>
    </div>
  );
}

export default RegistroCliente;
