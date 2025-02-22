// Plugin del calendario
(function ($) {
  function calendar($el, options) {
    var defaults = {
      date: new Date(),
      months: [
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre",
      ],
      days: ["Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb"],
      onMonthChange: function (date) {},
    };

    var settings = $.extend({}, defaults, options);
    var currentDate = settings.date;

    function drawCalendar() {
      var month = currentDate.getMonth();
      var year = currentDate.getFullYear();
      var firstDay = new Date(year, month, 1);
      var lastDay = new Date(year, month + 1, 0);

      var calendarHtml = `
        <div class="row">
          <div class="col-md-8">
            <div class="calendar">
              <table class="calendar-table table table-bordered">
                <thead>
                  <tr>
                    <th colspan="7">
                      <div class="d-flex justify-content-between align-items-center p-2">
                        <div class="btn-group">
                          <button class="btn btn-sm btn-primary prev-month">&lt;</button>
                          <button class="btn btn-sm btn-primary today">Hoy</button>
                          <button class="btn btn-sm btn-primary next-month">&gt;</button>
                        </div>
                        <h5 class="mb-0">${settings.months[month]} ${year}</h5>
                      </div>
                    </th>
                  </tr>
                  <tr>
                    ${settings.days.map((day) => `<th>${day}</th>`).join("")}
                  </tr>
                </thead>
                <tbody>
          `;

      let date = new Date(firstDay);
      date.setDate(date.getDate() - date.getDay());

      for (let i = 0; i < 6; i++) {
        calendarHtml += "<tr>";
        for (let j = 0; j < 7; j++) {
          let classes = ["calendar-day"];
          if (date.getMonth() !== month) classes.push("outside");
          if (date.toDateString() === new Date().toDateString())
            classes.push("today");

          calendarHtml += `
            <td class="${classes.join(" ")}" data-date="${date.toISOString()}"
              onclick="mostrarDetalles('${date.toISOString()}')">
              <div class="date">${date.getDate()}</div>
            </td>
          `;
          date.setDate(date.getDate() + 1);
        }
        calendarHtml += "</tr>";
      }

      calendarHtml += "</tbody></table></div>";
      $el.html(calendarHtml);

      // Marcar días con reservas
      Object.keys(reservasPorFecha).forEach(function (fechaKey) {
        const fecha = new Date(fechaKey);
        $el
          .find(`td[data-date^="${fechaKey}"]`)
          .addClass("tiene-reservas")
          .append(
            `<div class="reserva-indicator">${reservasPorFecha[fechaKey].length}</div>`
          );
      });
    }

    // Event handlers
    $el.on("click", ".prev-month", function () {
      currentDate.setMonth(currentDate.getMonth() - 1);
      drawCalendar();
      settings.onMonthChange(currentDate);
    });

    $el.on("click", ".next-month", function () {
      currentDate.setMonth(currentDate.getMonth() + 1);
      drawCalendar();
      settings.onMonthChange(currentDate);
    });

    $el.on("click", ".today", function () {
      currentDate = new Date();
      drawCalendar();
      settings.onMonthChange(currentDate);
    });

    $el.on("click", ".calendar-day", function () {
      const fecha = $(this).data('fecha');
      if (fecha) {
        $('.calendar-day').removeClass('selected');
        $(this).addClass('selected');
        mostrarDetalles(fecha);
      }
    });

    // Inicializar calendario
    drawCalendar();
  }

  $.fn.calendar = function (options) {
    return this.each(function () {
      calendar($(this), options);
    });
  };
})(jQuery);

// Inicialización del calendario y funciones relacionadas
function initCalendar($container, reservas) {
  const meses = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
  const diasSemana = ["Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"];
  let fechaActual = new Date();
  let reservasMap = new Map();

  // Crear mapa de reservas por fecha
  reservas.forEach((reserva) => {
    const fechaInicio = new Date(reserva.fechadesde);
    const fechaFin = new Date(reserva.fechahasta);
    
    // Ajustar a hora local
    fechaInicio.setMinutes(fechaInicio.getMinutes() + fechaInicio.getTimezoneOffset());
    fechaFin.setMinutes(fechaFin.getMinutes() + fechaFin.getTimezoneOffset());
    
    for (let fecha = new Date(fechaInicio); fecha <= fechaFin; fecha.setDate(fecha.getDate() + 1)) {
      // Usar fecha en formato ISO sin ajustes UTC
      const fechaKey = new Date(fecha.getTime() - (fecha.getTimezoneOffset() * 60000))
                        .toISOString().split('T')[0];
      
      if (!reservasMap.has(fechaKey)) {
        reservasMap.set(fechaKey, []);
      }
      reservasMap.get(fechaKey).push(reserva);
    }
  });

  function renderCalendario(mes, año) {
    const primerDia = new Date(año, mes, 1);
    const ultimoDia = new Date(año, mes + 1, 0);
    const diasEnMes = ultimoDia.getDate();
    const primerDiaSemana = (primerDia.getDay() + 6) % 7;

    // Actualizar encabezado del mes
    $container.find('.mes-actual').text(`${meses[mes]} ${año}`);

    // Renderizar días de la semana
    let diasSemanaHTML = '';
    diasSemana.forEach(dia => {
      diasSemanaHTML += `<th>${dia}</th>`;
    });
    $container.find('.dias-semana').html(diasSemanaHTML);

    // Renderizar días del mes
    let html = '';
    let contadorDias = 1;
    let diasTotales = primerDiaSemana + diasEnMes;
    let semanas = Math.ceil(diasTotales / 7);

    for (let i = 0; i < semanas; i++) {
      html += '<tr>';
      for (let j = 0; j < 7; j++) {
        if ((i === 0 && j < primerDiaSemana) || contadorDias > diasEnMes) {
          html += '<td class="calendar-day outside"></td>';
        } else {
          // Crear fecha usando hora local
          const fecha = new Date(año, mes, contadorDias);
          const fechaLocal = new Date(fecha.getTime() - (fecha.getTimezoneOffset() * 60000));
          const fechaKey = fechaLocal.toISOString().split('T')[0];
          
          const tieneReservas = reservasMap.has(fechaKey);
          const esHoy = new Date().toDateString() === fecha.toDateString();
          const cantidadReservas = tieneReservas ? reservasMap.get(fechaKey).length : 0;

          html += `
            <td class="calendar-day${esHoy ? ' today' : ''}${tieneReservas ? ' tiene-reservas' : ''}" 
                data-fecha="${fechaKey}"
                onclick="mostrarDetalles('${fechaKey}')">
                <div class="date">${contadorDias}</div>
                ${tieneReservas ? `<div class="reserva-indicator">${cantidadReservas}</div>` : ''}
            </td>
          `;
          contadorDias++;
        }
      }
      html += '</tr>';
    }
    $container.find('.calendar-body').html(html);
  }

  // Event listeners
  $container.find(".btn-anterior-mes").click(() => {
    fechaActual.setMonth(fechaActual.getMonth() - 1);
    renderCalendario(fechaActual.getMonth(), fechaActual.getFullYear());
  });

  $container.find(".btn-siguiente-mes").click(() => {
    fechaActual.setMonth(fechaActual.getMonth() + 1);
    renderCalendario(fechaActual.getMonth(), fechaActual.getFullYear());
  });

  $container.on('click', '.calendar-day', function() {
    const fecha = $(this).data('fecha');
    if (fecha) {
      $('.calendar-day').removeClass('selected');
      $(this).addClass('selected');
      mostrarDetalles(fecha);
    }
  });

  // Función para mostrar detalles de las reservas
  window.mostrarDetalles = function(fecha) {
    const reservasDia = reservasMap.get(fecha) || [];
    const fechaObj = new Date(fecha);
    const fechaFormateada = fechaObj.toLocaleDateString('es-ES', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });

    let html = `
      <div class="card reservas-detalle-card mb-4">
          <div class="card-header bg-danger text-white">
              <h5 class="mb-0">
                  <i class="fas fa-calendar-day mr-2"></i>
                  Reservas para el ${fechaFormateada}
              </h5>
          </div>
          <div class="card-body">`;

    if (!reservasDia || reservasDia.length === 0) {
        html += `
            <div class="text-center py-4">
                <i class="fas fa-calendar-times fa-3x text-muted mb-3"></i>
                <p class="text-muted">No hay reservas para este día</p>
            </div>`;
    } else {
        reservasDia.forEach(reserva => {
            html += `
                <div class="card mb-3">
                    <div class="card-body">
                        <h6 class="card-title" style="color: #dc3545;">
                            <i class="fas fa-chalkboard mr-2" style="color: #dc3545;"></i>
                            ${reserva.aula.descripcion}
                        </h6>
                        <div class="row">
                            <div class="col-md-6">
                                <p class="mb-1">
                                    <i class="fas fa-book mr-2"></i>
                                    Curso: ${reserva.curso.descripcion}
                                </p>
                                <p class="mb-1">
                                    <i class="fas fa-clock mr-2"></i>
                                    Horario: ${reserva.horadesde.substring(0,5)} - ${reserva.horahasta.substring(0,5)}
                                </p>
                            </div>
                            <div class="col-md-6 text-right">
                                <a href="/user/reserva/${reserva.idreserva}/editar" 
                                   class="btn btn-sm btn-primary">
                                    <i class="fas fa-edit"></i> Editar
                                </a>
                                <form action="/user/reserva/${reserva.idreserva}/eliminar" 
                                      method="POST" 
                                      class="d-inline"
                                      onsubmit="return confirm('¿Confirmas la eliminación?');">
                                    <button type="submit" class="btn btn-sm btn-danger ml-2">
                                        <i class="fas fa-trash"></i> Eliminar
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>`;
        });
    }

    html += `</div></div>`;
    $('#reservas-detalle').html(html);
  }

  // Inicializar con el mes actual y mostrar detalles del día actual si hay reservas
  renderCalendario(fechaActual.getMonth(), fechaActual.getFullYear());
  const hoy = new Date();
  const fechaHoyKey = new Date(hoy.getTime() - (hoy.getTimezoneOffset() * 60000))
                        .toISOString().split('T')[0];
  if (reservasMap.has(fechaHoyKey)) {
    mostrarDetalles(fechaHoyKey);
    $(`[data-fecha="${fechaHoyKey}"]`).addClass('selected');
  }
}

// Exponer la función initCalendar globalmente
window.initCalendar = initCalendar; 