window.setInterval(function() {
    var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			update(xhttp);
		}
	}
	xhttp.open("GET", "http://labs.casaro.de/doNation/get.php", true);
	xhttp.send();
}, 1000);

function update(xml)
{
	var parser = new DOMParser();
	var xmlDoc = parser.parseFromString(xml.response, "application/xml");
	for (var i = 0; i < xmlDoc.getElementsByTagName("entity").length; i++)
	{
		var entity = xmlDoc.getElementsByTagName("entity")[i];
		var id = entity.getElementsByTagName("id")[0].innerHTML;
		var percent = entity.getElementsByTagName("percent")[0].innerHTML;
		
		if (document.getElementById(id) == null)
		{
			//Create a new row for tbody
			var tr = document.createElement('tr');
			tr.id = id;
			
			var td_organization = document.createElement('td');
			td_organization.innerHTML = entity.getElementsByTagName("organization")[0].innerHTML;
			var td_item = document.createElement('td');
			td_item.innerHTML = entity.getElementsByTagName("item")[0].innerHTML;
			var td_percent = document.createElement('td');
			td_percent.innerHTML = percent;
			var td_taker = document.createElement('td');
			td_taker.innerHTML = "<input type='text' id='taker_" + id + "'>";
			var td_submit = document.createElement('td');		
			td_submit.innerHTML = "<input type='button' Value='Collected' onclick='collected(" + id + ");'>";
			
			tr.appendChild(td_organization);
			tr.appendChild(td_item);
			tr.appendChild(td_percent);
			
			if (percent == 100)
			{
				tr.appendChild(td_taker);
				tr.appendChild(td_submit);
			}		
			
			document.getElementById('backend').appendChild(tr);
		}
	}
}

function collected(id)
{
	var taker = document.getElementById('taker_' + id).value;
	if (taker == "")
	{ 
		return;
	}
	
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			document.getElementById(id).remove();
		}
	}
	xhttp.open("GET", "http://labs.casaro.de/doNation/collect.php?id=" + id + "&taker=" + taker, true);
	xhttp.send();
}