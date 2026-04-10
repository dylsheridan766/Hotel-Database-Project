<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
  <meta charset="UTF-8">
  <link href="https://fonts.googleapis.com/css2?family=Hind&family=Source+Sans+3&display=swap" rel="stylesheet">
  <style>
    body {
      font-family: 'Source Sans 3', sans-serif;
    }
  </style>
</head>
<body>


  <div style="display: flex; align-items: center; gap: 8px;">
    <i class="fa-regular fa-key" style="font-size: 20px;"></i>
    <h3 style="margin: 0;color: #B11116;">Fait une Location</h3>
  </div>

  <form action="testdb" method="GET">
    <div>
      Room ID: <input type="number" name="rID" placeholder="e.x. 54">
    </div>
    <div>
      Client Email: <input type="text" name="cEM" placeholder="e.x. shmebulock@gmail.com">
    </div>
    <div>
      Employee ID: <input type="number" name="empID" placeholder="e.x. 54 (optionnel)">
    </div>
    <div>
      End Date: <input type="date" name="endate">
    </div>
    <input type="hidden" name="action" value="locappend">
    <button type="submit">Soumettre</button>
  </form>
  <a href="index.jsp" style="text-decoration: none;">
    <button type="button">Retourner</button>
  </a>
</body>
</html>