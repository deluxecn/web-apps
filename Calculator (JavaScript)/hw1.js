function add() {
	var x = parseFloat(document.getElementById("x").value);
	var y = parseFloat(document.getElementById("y").value);
	document.getElementById('ans').value = x + y;
}

function minus() {
	var x = parseFloat(document.getElementById("x").value);
	var y = parseFloat(document.getElementById("y").value);
	document.getElementById('ans').value = x - y;
}

function multiply() {
	var x = parseFloat(document.getElementById("x").value);
	var y = parseFloat(document.getElementById("y").value);
	document.getElementById('ans').value = x * y;
}

function divide() {
	var x = parseFloat(document.getElementById("x").value);
	var y = parseFloat(document.getElementById("y").value);
	document.getElementById('ans').value = x / y;
}