function filter() {
  var titulo, autor, ano, precio, table, tr, tdTitulo, tdAutor, tdAno, tdPrecio, i, count;

  titulo = document.getElementById("stitulo").value.toUpperCase();
  autor = document.getElementById("sautor").value.toUpperCase();
  ano = document.getElementById("sano").value.toUpperCase();
  if(document.getElementById("sprecio").value === "") precio = 0.;
  else precio = parseFloat(document.getElementById("sprecio").value);
  table = document.getElementById("catalogo");
  tr = table.getElementsByTagName("tr");

  for (i = 1; i < tr.length; i++) {
    count = -4;
    tdTitulo = tr[i].getElementsByTagName("td")[0];
    tdAutor = tr[i].getElementsByTagName("td")[1];
    tdAno = tr[i].getElementsByTagName("td")[3];
    tdPrecio = tr[i].getElementsByTagName("td")[4];
    
    if (tdTitulo) {
      if (tdTitulo.innerHTML.toUpperCase().indexOf(titulo) > -1) {
        count++;
      }
    }
    if (tdAutor) {
      if (tdAutor.innerHTML.toUpperCase().indexOf(autor) > -1) {
        count++;
      }
    }
    if (tdAno) {
      if (tdAno.innerHTML.toUpperCase().indexOf(ano) > -1) {
        count++;
      }
    }
    if (tdPrecio) {
      if ((precio >= parseFloat(tdPrecio.innerHTML)) || precio === 0) {
        count++;
      }
    }
    if(count >= 0) tr[i].style.display = "";
    else tr[i].style.display = "none";
  }
}