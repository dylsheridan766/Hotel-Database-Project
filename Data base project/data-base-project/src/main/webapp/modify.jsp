<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
  <meta charset="UTF-8">
  <link href="https://fonts.googleapis.com/css2?family=Hind&family=Source+Sans+3&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
  <style>
    body {
      font-family: 'Source Sans 3', sans-serif;
    }
    .container { margin: 20px; }
  </style>
</head>
<body>
<div class="container">


  <div style="display: flex; align-items: center; gap: 8px;">
    <i class="fa-regular fa-pen-to-square" style="font-size: 20px;"></i>
    <h3 style="margin: 0;color: #B11116;">Modifier le Database</h3>
  </div>

  <p>Choisir un table afin de voir sa contenue</p>
  <form action="testdb" method="GET">
    <input type="hidden" name="action" value="viewTable">
    <div>
      <input type="radio" name="tableName" value="Hotels" required> Hotêls<br>
      <input type="radio" name="tableName" value="Chambres"> Chambres<br>
      <input type="radio" name="tableName" value="Employes"> Employés<br>
      <input type="radio" name="tableName" value="Clients"> Clients<br>
    </div>
    <br>
    <button type="submit">Voir les Données</button>
  </form>
  <a href="index.jsp" style="text-decoration: none;">
    <button type="button">Retourner</button>
  </a>
</div>
</body>
</html>