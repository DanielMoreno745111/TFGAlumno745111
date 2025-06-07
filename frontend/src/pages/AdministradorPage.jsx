import React, { useEffect, useState } from 'react';
import {
  obtenerAbogados,
  obtenerClientesDeAbogado,
  obtenerSolicitudes,
  aceptarSolicitud,
  eliminarSolicitud
} from '../components/Fetch.jsx';
import './Admin.css';
import { useNavigate } from 'react-router-dom';
import { logout } from '../components/Logout';

function AdminPanel() {
  const [abogados, setAbogados] = useState([]);
  const [clientes, setClientes] = useState([]);
  const [solicitudes, setSolicitudes] = useState([]);
  const [selectedAbogado, setSelectedAbogado] = useState(null);

  const navigate = useNavigate();
  const token = localStorage.getItem('token'); 
  useEffect(() => {
    fetchAbogados();
    fetchSolicitudes();
  }, []);

  const fetchAbogados = async () => {
    try {
      const data = await obtenerAbogados(token);
      setAbogados(data);
    } catch (error) {
      console.error("Error al obtener abogados:", error);
    }
  };

  const fetchSolicitudes = async () => {
    try {
      const data = await obtenerSolicitudes(token);
      if (data.noData) {
        setSolicitudes([]); 
      } else {
        setSolicitudes(data);
      }
    } catch (error) {
      console.error("Error al obtener solicitudes:", error);
      setSolicitudes([]); 
    }
  }

const handleVerClientes = async (dni, numeroColegiando) => {
  const abogadoKey = `${dni} - ${numeroColegiando}`;
  if (selectedAbogado === abogadoKey) {
    setClientes([]);
    setSelectedAbogado(null);
  } else {
    try {
      const data = await obtenerClientesDeAbogado(token, dni, numeroColegiando);
      setClientes(data);
      setSelectedAbogado(abogadoKey);
    } catch (error) {
      console.error("Error al obtener clientes del abogado:", error);
    }
  }
};


  const handleAceptarSolicitud = async (id) => {
    try {
      await aceptarSolicitud(token, id);
      fetchSolicitudes();
    } catch (error) {
      console.error("Error al aceptar solicitud:", error);
    }
  };

  const handleEliminarSolicitud = async (id) => {
    try {
      await eliminarSolicitud(token, id);
      fetchSolicitudes();
    } catch (error) {
      console.error("Error al eliminar solicitud:", error);
    }
  };

  const handleLogout = () => {
    logout(navigate)
  }

  return (
    <div className="admin-container">
      <div>
        <button className="cerrarSesioN" onClick={handleLogout}>Cerrar sesión</button>
      </div>
      <h1>Saludos Administrador</h1>
      <h2>Listado de Abogados</h2>
      <table className="abogados-table">
        <thead>
          <tr>
            <th>DNI</th>
            <th>Nombre</th>
            <th>Correo</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {abogados.map((abogado) => (
            <tr key={abogado.id.dni + abogado.id.numeroColegiando}>
              <td>{abogado.id.dni}</td>
              <td>{abogado.nombre}</td>
              <td>{abogado.correo}</td>
              <td>
                <button onClick={() => handleVerClientes(abogado.id.dni, abogado.id.numeroColegiando)}>
                  {selectedAbogado === `${abogado.id.dni} - ${abogado.id.numeroColegiando}`? 'Ocultar Clientes' : 'Ver Clientes'}
                  </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {clientes.length > 0 && (
        <div className="clientes-section">
          <h2>Clientes del abogado {selectedAbogado}</h2>
          <table className="abogados-table">
            <thead>
              <tr>
                <th>DNI</th>
                <th>Nombre</th>
                <th>Correo</th>
                <th>Teléfono</th>
              </tr>
            </thead>
            <tbody>
              {clientes.map((cliente) => (
                <tr key={cliente.dni}>
                  <td>{cliente.dni}</td>
                  <td>{cliente.nombre}</td>
                  <td>{cliente.correo}</td>
                  <td>{cliente.telefono}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <h2>Solicitudes de Registro</h2>
      <table className="solicitudes-table">
        <thead>
          <tr>
            <th>Email</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {solicitudes.map((solicitud) => (
            <tr key={solicitud.id}>
              <td>{solicitud.mail}</td>
              <td>
                <button onClick={() => handleAceptarSolicitud(solicitud.id)}>Aceptar</button>
                <button onClick={() => handleEliminarSolicitud(solicitud.id)}>Eliminar</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AdminPanel;
