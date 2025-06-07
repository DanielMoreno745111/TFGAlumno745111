import { useState } from 'react'
import './App.css'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import RegistroPage  from './pages/RegistroPage.jsx';
import AdministradorPage from './pages/AdministradorPage.jsx'
import SolicitudPage from './pages/SolicitudPage.jsx'
import RegistroAbogadoPage from './pages/RegistroAbogadoPage.jsx';
import RegistroCliente from './pages/RegistroClientePage.jsx';
import Abogado from './pages/AbogadoPage.jsx';
import ClientePage from './pages/ClientePage.jsx';
function App() {
  const [count, setCount] = useState(0)

  return (
    <Router>
      <Routes>
        <Route path="/" element={<RegistroPage />}/>
        <Route path='/admin' element={<AdministradorPage/>}/>
        <Route path='/abogado' element={<Abogado/>}/>
        <Route path='/cliente' element={<ClientePage/>}/>
        <Route path='/solicitud' element={<SolicitudPage/>}/>
        <Route path='/registro-abogado' element={<RegistroAbogadoPage/>}/>
        <Route path='/registro-cliente' element={<RegistroCliente/>}/>
      </Routes>
    </Router>
  )
}

export default App
