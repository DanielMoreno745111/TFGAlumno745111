import React, { useEffect, useState } from 'react';
import { subirDocumentoCliente, obtenerDocumentosCliente, descargarDocumento } from '../components/Fetch';
import './ClientePageStyle.css';
import { useNavigate } from 'react-router-dom';
import { logout } from '../components/Logout';

function ClientePage() {
  const [documentos, setDocumentos] = useState([]);
  const [archivo, setArchivo] = useState(null);
  const [mensaje, setMensaje] = useState('');
  const [error, setError] = useState('');

  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  useEffect(() => {
    const fetchDocumentos = async () => {
      try {
        const data = await obtenerDocumentosCliente(token);
        setDocumentos(data);
      } catch (err) {
        setError('Error al obtener documentos');
      }
    };

    fetchDocumentos();
  }, [token]);

  const handleFileChange = (e) => {
    setArchivo(e.target.files[0]);
  };

  const handleUpload = async (e) => {
    e.preventDefault();
    setMensaje('');
    setError('');

    if (!archivo) {
      setError('Selecciona un archivo primero');
      return;
    }

    try {
      await subirDocumentoCliente(archivo, token);
      setMensaje('Archivo subido correctamente');
      setArchivo(null);
      document.getElementById('file-input').value = '';
      const data = await obtenerDocumentosCliente(token);
      setDocumentos(data);
    } catch (err) {
      setError('Error al subir el documento');
    }
  };

  const handleDownload = async (id, nombre) => {
    try {
      await descargarDocumento(id, nombre, token);
    } catch (err) {
      setError('Error al descargar documento');
    }
  };

  const handleLogout = () => {
    logout(navigate);
  };

  return (
    <div className="cliente-container">
      <div>
        <button className='cerrarSesion' onClick={handleLogout}>Cerrar sesión</button>
      </div>
      <h2>Mis Documentos</h2>

      

      {mensaje && <div className="mensaje">{mensaje}</div>}
      {error && <div className="error">{error}</div>}

      {documentos.length === 0 ? (
        <p>No tienes documentos.</p>
      ) : (
        <table className="tabla-documentos">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
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
                <td>{doc.fechaEntrega}</td>
                <td>{doc.fechaRespuesta ? doc.fechaRespuesta : '-'}</td>
                <td>
                  <button onClick={() => handleDownload(doc.id, doc.nombre)}>
                    Descargar
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      <form onSubmit={handleUpload} className="upload-form">
        <input type="file" id="file-input" onChange={handleFileChange} />
        <button type="submit">Subir Documento</button>
      </form>
      
    </div>
  );
}

export default ClientePage;
