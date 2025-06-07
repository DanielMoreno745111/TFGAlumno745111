import React, { useState } from 'react';
import { enviarSolicitud } from '../components/Fetch.jsx'; // ruta según tu proyecto
import './SolicitudStyle.css';

function SolicitudPage() {
  const [dni, setDni] = useState('');
  const [numeroColegiado, setNumeroColegiado] = useState('');
  const [mail, setMail] = useState('');
  const [mensaje, setMensaje] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMensaje('');
    setError('');

    try {
      await enviarSolicitud({ dni, numeroColegiado, mail });
      setMensaje('Solicitud enviada correctamente.');
      setDni('');
      setNumeroColegiado('');
      setMail('');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="solicitud-container">
      <form className="solicitud-form" onSubmit={handleSubmit}>
        <h2>Solicitud de Registro para Abogados</h2>

        <label htmlFor="dni">DNI</label>
        <input
          type="text"
          id="dni"
          value={dni}
          onChange={(e) => setDni(e.target.value)}
          required
        />

        <label htmlFor="numeroColegiado">Número de Colegiado</label>
        <input
          type="text"
          id="numeroColegiado"
          value={numeroColegiado}
          onChange={(e) => setNumeroColegiado(e.target.value)}
          required
        />

        <label htmlFor="mail">Correo Electrónico</label>
        <input
          type="email"
          id="mail"
          value={mail}
          onChange={(e) => setMail(e.target.value)}
          required
        />

        <button type="submit">Enviar Solicitud</button>

        {mensaje && <div className="mensaje-exito">{mensaje}</div>}
        {error && <div className="mensaje-error">{error}</div>}
      </form>
    </div>
  );
}

export default SolicitudPage;
