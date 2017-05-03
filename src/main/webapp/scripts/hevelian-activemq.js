/**
 * handles the navbar switching between pages
 * @param _switchTo
 */
function NavSwitcher(_switchTo) {
	
	document.getElementById("container_contacts").style.display = 'none';
	document.getElementById("container_visits").style.display = 'none';
	document.getElementById("container_calculator").style.display = 'none';
	
	document.getElementById("container_" + _switchTo).style.display = 'inline-block';
	
	document.getElementById("nav_contacts").setAttribute("class", "");
	document.getElementById("nav_visits").setAttribute("class", "");
	document.getElementById("nav_calculator").setAttribute("class", "");

	document.getElementById("nav_" + _switchTo).setAttribute("class", "active");
}

function loadQueues() {
	//do nothing for now ... do something shortly :)
}