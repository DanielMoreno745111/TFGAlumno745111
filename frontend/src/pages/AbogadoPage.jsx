import React, { useEffect, useState } from 'react';
import {
  obtenerClientes,
  obtenerDocumentosPorCliente,
  enviarEmailRegistroCliente,
  descargarDocumento
} from '../components/Fetch.jsx';
import './AbogadoStyle.css';
import { useNavigate } from 'react-router-dom';
import { logout } from '../components/Logout';

function Abogado() {
  const [clientes, setClientes] = useState([]);
  const [documentos, setDocumentos] = useState([]);
  const [clienteSeleccionado, setClienteSeleccionado] = useState(null);
  const [mensaje, setMensaje] = useState('');
  const [error, setError] = useState('');
  const [correoRegistro, setCorreoRegistro] = useState('');
  const [mostrarFormulario, setMostrarFormulario] = useState(false);
  const [mostrandoBotonSumar, setMostrandoBotonSumar] = useState(true);

  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  useEffect(() => {
    if (!token) {
      setError('No autorizado. Por favor, inicia sesión.');
      return;
    }

    obtenerClientes(token)
      .then(data => setClientes(data))
      .catch(() => setError('Error al cargar clientes'));
  }, [token]);

  const cargarDocumentos = (dniCliente) => {
    setError('');
    setMensaje('');
    setClienteSeleccionado(dniCliente);
    setMostrandoBotonSumar(true); 

    obtenerDocumentosPorCliente(token, dniCliente)
      .then(data => {
        if (data.noData) {
          setDocumentos([]);
          setMensaje('No hay documentos para este cliente.');
        } else {
          setDocumentos(data);
          setMensaje('');
          const algunConRespuesta = data.some(doc => doc.fechaRespuesta);
          if (algunConRespuesta) setMostrandoBotonSumar(false);
        }
      })
      .catch((error) => {
        setError('Error al cargar documentos');
        setDocumentos([]);
      });
  };

  const handleEnviarEmail = async (e) => {
    e.preventDefault();
    setMensaje('');
    setError('');

    try {
      const msg = await enviarEmailRegistroCliente(token, correoRegistro);
      setMensaje(msg);
      setCorreoRegistro('');
    } catch (err) {
      setError(err.message || 'Error al enviar el correo');
    }
  };

  
  const sumarQuinceDiasFechaRespuesta = async () => {
    if (!clienteSeleccionado) return;

    try {
      await sumarQuinceDiasFechaRespuesta(token, clienteSeleccionado);
      await cargarDocumentos(clienteSeleccionado);
      setMostrandoBotonSumar(false);
      setMensaje('Se sumaron 15 días a la fecha respuesta de los documentos.');
    } catch (error) {
      setError('Error al sumar 15 días');
    }
  };

  const handleDescargarDocumento = async (id, nombre) => {
    try {
      await descargarDocumento(id, nombre, token);
    } catch (error) {
      setError('Error al descargar documento');
    }
  };

  const toggleFormulario = () => setMostrarFormulario(prev => !prev);

  const handleLogout = () => logout(navigate);

  return (
    <div className="abogado-container">
      <div>
        <button className='cerrarSesion' onClick={handleLogout}>Cerrar sesión</button>
      </div>
      <h2>Bienvenido, Abogado</h2>

      {error && <div className="error">{error}</div>}
      {mensaje && <div className="mensaje">{mensaje}</div>}

      <section>
        <h3>Clientes asociados</h3>
        {clientes.length === 0 ? (
          <p>No tienes clientes asignados.</p>
        ) : (
          <ul>
            {clientes.map(({ dni, nombre, correo }) => (
              <li key={dni}>
                {nombre} - {dni} - {correo}
                <button onClick={() => cargarDocumentos(dni)}>
                  Ver documentos
                </button>
              </li>
            ))}
          </ul>
        )}
      </section>

      <section>
        <h3>Documentos del cliente {clienteSeleccionado}</h3>

        {documentos.length === 0 ? (
          <p>No hay documentos para este cliente.</p>
        ) : (
          <>
            <table className="tabla-documentos">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nombre</th>
                  <th>URL</th>
                  <th>Fecha Entrega</th>
                  <th>Fecha Respuesta</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {documentos.map(doc => (
                  <tr key={doc.id}>
                    <td>{doc.id}</td>
                    <td>{doc.nombre}</td>
                    <td>
                      <a href={doc.url} target="_blank" rel="noopener noreferrer">
                        Ver documento
                      </a>
                    </td>
                    <td>{doc.fechaEntrega}</td>
                    <td>{doc.fechaRespuesta ? doc.fechaRespuesta : '-'}</td>
                    <td>
                      <button className="botonDescarga" onClick={() => handleDescargarDocumento(doc.id, doc.nombre)}>
                        Descargar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>

            {mostrandoBotonSumar && (
              <button onClick={sumarQuinceDiasFechaRespuesta}
              >
                +15 días a fecha respuesta
              </button>
            )}
          </>
        )}
      </section>

      <section>
        <button className="btn-toggle-formulario" onClick={toggleFormulario}>
          {mostrarFormulario ? 'Ocultar formulario de registro' : 'Registrar nuevo cliente'}
        </button>

        {mostrarFormulario && (
          <form onSubmit={handleEnviarEmail} className='texto'>
            <input
              type="email"
              placeholder="Correo del cliente"
              value={correoRegistro}
              onChange={(e) => setCorreoRegistro(e.target.value)}
              required
            />
            <button type="submit" className="btn-enviar-email">
              Enviar enlace de registro
            </button>
          </form>
        )}
      </section>
    </div>
  );
}

export default Abogado;
