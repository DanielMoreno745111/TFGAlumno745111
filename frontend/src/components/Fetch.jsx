export async function loginUsuario(dni, contrasena, rol) {
  const body = JSON.stringify({ dni, contrasena, rol });
  console.log('Datos a enviar en fetch:', body);

  const response = await fetch('http://localhost:8080/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body
  });

  console.log('Respuesta recibida:', response.status);

  const text = await response.text();

  try {
    const data = JSON.parse(text);
    console.log('Datos recibidos:', data);
    return data.token;
  } catch (e) {
    console.error('Respuesta no JSON, contenido recibido:', text);
    throw new Error('Login fallido, respuesta inesperada del servidor');
  }
}

export async function fetchConToken(url, method = 'GET', token, body = null) {
  const opciones = {
    method,
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  };

  if (body) {
    opciones.headers['Content-Type'] = 'application/json';
    opciones.body = JSON.stringify(body);
  }

  try {
    const response = await fetch(url, opciones);

    const contentType = response.headers.get('content-type');
    if (!contentType || !contentType.includes('application/json')) {
      console.warn('Fetch.jsx:54 Respuesta no es JSON, se devuelve vacía.');
      return { noData: true };
    }

    const data = await response.json();

    if (!response.ok) {
      console.error('Error HTTP:', response.status, data);
      throw new Error(data.message || 'Error en la petición');
    }

    return data;
  } catch (error) {
    console.error('Error en fetchConToken:', error);
    throw error;
  }
}



export function obtenerAbogados(token) {
  return fetchConToken('http://localhost:8080/admin/abogados', 'GET', token);
}

export function obtenerClientesDeAbogado(token, dni, numeroColegiando) {
  return fetchConToken(`http://localhost:8080/admin/clientesabogado?dni=${dni}&numeroColegiando=${numeroColegiando}`, 'GET', token);
}

export function obtenerSolicitudes(token) {
  return fetchConToken('http://localhost:8080/admin/solicitudes', 'GET', token);
}

export function aceptarSolicitud(token, id) {
  return fetchConToken(`http://localhost:8080/admin/aceptarsolicitud?id=${id}`, 'PUT', token);
}

export function eliminarSolicitud(token, id) {
  return fetchConToken(`http://localhost:8080/admin/eliminar-solicitud?id=${id}`, 'DELETE', token);
}

export async function enviarSolicitud(solicitud) {
  const response = await fetch('http://localhost:8080/registro/solicitud', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(solicitud)
  });

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || 'Error al enviar la solicitud');
  }

  return response.text(); 
}

export async function registrarAbogado(datosAbogado) {
  const url = 'http://localhost:8080/registro/registro-abogado';  
  try {
    const resp = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(datosAbogado),
    });

    if (!resp.ok) {
      throw new Error('Error en registro');
    }

    const data = await resp.json();
    return data;
  } catch (error) {
    throw error;
  }
}


export function obtenerClientes(token) {
  return fetchConToken('http://localhost:8080/abogado/clientes', 'GET', token);
}

export function obtenerDocumentosPorCliente(token, dniCliente) {
  const url = new URL('http://localhost:8080/abogado/documentos');
  url.searchParams.append('dniCliente', dniCliente);
  console.log("URL generada para documentos:", url.toString());

  return fetch(url, {
    method: 'GET',
    headers: {
      'Authorization': 'Bearer ' + token,
      'Content-Type': 'application/json'
    }
  })
  .then(res => {
    if (!res.ok) {
      if (res.status === 404) return { noData: true };
      throw new Error('Error en la petición');
    }
    return res.json();
  })
  .then(data => {
    console.log("Datos recibidos del backend:", data);
    return data;
  });
}





export async function registrarCliente(cliente, token) {
  const response = await fetch('http://localhost:8080/registro/registro-cliente', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify(cliente),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Error al registrar cliente');
  }

  return response.json();
}


export async function enviarEmailRegistroCliente(token, correo) {
  const response = await fetch(`http://localhost:8080/abogado/registro-cliente?correo=${correo}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });

  if (!response.ok) {
    const mensaje = await response.text();
    throw new Error(mensaje || 'Error al enviar email');
  }

  return await response.text(); 
}


export async function getDocumentosCliente(token) {
  const response = await fetch('http://localhost:8080/cliente/documentos', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || 'Error al obtener documentos del cliente');
  }

  const contentType = response.headers.get("Content-Type");

  if (contentType && contentType.includes("application/json")) {
    return response.json();
  }

  return [];
}



export async function obtenerDocumentosCliente(token) {
  const res = await fetch(`http://localhost:8080/cliente/documentos`, {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export async function subirDocumentoCliente(archivo, token) {
  const formData = new FormData();
  formData.append('file', archivo);

  const res = await fetch(`http://localhost:8080/documentos/subida-cliente`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
    body: formData,
  });

  if (!res.ok) throw new Error(await res.text());
  return res.text();
}

export async function descargarDocumento(id, nombre, token) {
  const res = await fetch(`http://localhost:8080/documentos/descargar/${id}`, {
    headers: {
      'Authorization': `Bearer ${token}`,
    },
  });

  if (!res.ok) throw new Error(await res.text());

  const blob = await res.blob();
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', nombre);
  document.body.appendChild(link);
  link.click();
  link.remove();
}

export async function subirDocumentoAbogado(dniCliente, archivo, token) {
  const formData = new FormData();
  formData.append('archivo', archivo);

  const res = await fetch(`http://localhost:8080/documentos/subida-abogado/${dniCliente}`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
    },
    body: formData,
  });

  if (!res.ok) throw new Error(await res.text());
  return res.text();
}

export async function sumarQuinceDiasFechaRespuesta(token, id) {
  const res = await fetch(`http://localhost:8080/documentos/presentado/${id}`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json'
    }
  });
  if (!res.ok) throw new Error('Error al actualizar la fecha de respuesta');
  return res.json();
}