import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { loginUsuario } from '../components/Fetch.jsx';
import './RegistroStyle.css';

function Login() {
  const [dni, setDni] = useState('');
  const [contrasena, setContrasena] = useState('');
  const [rol, setRol] = useState('ADMIN');
  const [error, setError] = useState('');
  const navigate = useNavigate();
    console.log('Enviando datos:', { dni, contrasena, rol }); 

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    console.log('Enviando datos:', { dni, contrasena, rol }); 
    try {
      const token = await loginUsuario(dni, contrasena, rol);
      localStorage.setItem('token', token);
      if (rol === "ADMIN") navigate("/admin");
      else if (rol === "ABOGADO") navigate("/abogado");
      else navigate("/cliente");    } catch (err) {
      setError('Usuario o contraseña incorrectos');
    }
  };

  return (
    <div className="login-container">
      <form className="login-form" onSubmit={handleSubmit}>
        <h2>Iniciar Sesión</h2>

        <label htmlFor="dni">DNI</label>
        <input
          type="text"
          id="dni"
          value={dni}
          onChange={(e) => setDni(e.target.value)}
          required
        />

        <label htmlFor="contrasena">Contraseña</label>
        <input
          type="password"
          id="contrasena"
          value={contrasena}
          onChange={(e) => setContrasena(e.target.value)}
          required
        />

        <label htmlFor="rol">Rol</label>
        <select id="rol" value={rol} onChange={(e) => setRol(e.target.value)}>
          <option value="ADMIN">Administrador</option>
          <option value="ABOGADO">Abogado</option>
          <option value="CLIENTE">Cliente</option>
        </select>

        <button type="submit">Ingresar</button>

        {error && <div className="error">{error}</div>}

        <div className="registro-enlace">
          ¿Eres abogado? <a href="/solicitud">Solicita un usuario</a>
        </div>
      </form>
    </div>
  );
}

export default Login;
