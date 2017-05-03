/**
 * This file contains a set of AJAX routines which make the UI just that little bit more
 * user-friendly. All the appliction-specific functions are organised into pairs with a
 * 'get' function and a 'process' function (for processing the results).
 */

function GetURLParameter(_name)
{
	var _url = new String(document.location.href);
	
	try
	{
	var _parts = _url.split("?")[1].split("&");
	} catch(_e) { return ''; }
	
	for(var i=0; i<_parts.length; i++)
	{
		var _arg = _parts[i].split('=');
		if(_arg[0] == _name)
		{
			return _arg[1];			// we found the value, so lets return it
		}
	}
	
	return '';						// we found nothing, so we return an empty string
}

function GetURL(_url)
{
	var _split 		= _url.split('?');
	var _random 	= Math.random();
	var _opts		= '';
	
	if(_split.length > 1)	
	{
		var _args		= _split[1].split('&');
		for(var i=0; i<_args.length; i++)
		{
			var _get = _args[i].split('=');
			
			if(_get[0] == 'RANDOMISER') continue;		// remove the random value if exists
			if(i>0)  _opts = _opts.concat('&');					// add delimiter
			
			_opts = _opts.concat(_get[0], '=', _get[1]);		// add the options
		}
		
		_opts = _opts.concat('&RANDOMISER=', _random);			// add the randomiser value
		
		return _split[0] + '?' + _opts;
	} else
	{
		return _url + '?RANDOMISER=' + _random;
	}
}

function AJAX()
{
	this.GetNowAsText					= _getNowAsText;
	this.GetNowAsXML					= _getNowAsXML;
	this.GetResultAsXML					= _getResultAsXML;

	/**
	 * GetResultAsXML()
	 * Takes a text representation of XML and converts it to a DOM object tree
	 * 
	 * @param _text
	 * @returns {sisXMLDocument}
	 */
	function _getResultAsXML(_text)
	{
		return new sisXMLDocument(_text);
	}
	
	/**
	 * GetNowAsText()
	 * Uses AJAX to fetch the results of the URL as text using the specified method.
	 * If the method is POST then the _postData is sent with the request.
	 * 
	 * @param _method
	 * @param _url
	 * @param _postData
	 * @returns
	 */
	function _getNowAsText(_method, _url, _postData)
	{
		var _ajax = new sisAJAXConnector();
		switch(_method)
		{
		case 'post':
		case 'POST':
		case 'put':
		case 'PUT':
			_ajax.open(_method, GetURL(_url), false);
			_ajax.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			_ajax.send(_postData);
			
			return _ajax.responseText;
			
		case 'get':
		case 'GET':
		case 'delete':
		case 'DELETE':
			_ajax.open(_method, GetURL(_url), false);
			_ajax.send(null);
			
			return _ajax.responseText;
		}
	}
	
	/**
	 * GetNowAsXML()
	 * Uses AJAX to fetch the results of the URL as XML DOM using the specified method.
	 * If the method is POST then the _postData is sent with the request.
	 * 
	 * @param _method
	 * @param _url
	 * @param _postData
	 * @returns
	 */
	function _getNowAsXML(_method, _url, _postData)
	{
		var _ajax = new sisAJAXConnector();
		switch(_method)
		{
		case 'post':
		case 'POST':
			_ajax.open(_method, GetURL(_url), false);
			_ajax.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
			_ajax.send(_postData);
			
			return this.GetResultAsXML(_ajax.responseText);
			
		case 'get':
		case 'GET':
			_ajax.open(_method, GetURL(_url), false);
			_ajax.send(null);
			
			return this.GetResultAsXML(_ajax.responseText);
		}
	}
}

/*******************************************************************************************************************
  * generic ajax and xml functions below
  *******************************************************************************************************************/

function get_nextsibling(n)
{
	try {
	var x=n.nextSibling;
	while (x!=null && x.nodeType!=1)
	{
		x=x.nextSibling;
	}
	return x;
	} catch(e) { return null; }
}

function HasChildNodes(n)
{
	for(var i=0; i<n.childNodes.length; i++)
	{
		if(n.childNodes[i].nodeType == 1) return true;
	}
	
	return false;
}

function getXMLNode(_document, _nodename, _default)
{
	try
	{
		var _data = _document.getElementsByTagName(_nodename)[0].firstChild.nodeValue;
		return _data;
	} catch(e)
	{
		if(_default == null) return "";
		return _default;
	}
}

function sisAJAXConnector()
{
	var xmlhttp;
	/*@cc_on
	@if(@_jscript_version >=5)
		try {
			xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try {
				xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (E) {
				xmlhttp = false;
				}
			}
	@else
	xmlhttp = false;
	@end @*/
	
	if(!xmlhttp && typeof XMLHttpRequest != 'undefined')
	{
		try {
				xmlhttp = new XMLHttpRequest();
			} catch(e) {
				xmlhttp = false;
			}
	}
	return xmlhttp;
}

function sisXMLDocument(_xml)
{
	
	var xmlDoc = null;
	
	try
	{
		xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async = "false";
		xmlDoc.loadXML(_xml);
		return xmlDoc;
	} catch(e)
	{
		var parser = new DOMParser();
		xmlDoc = parser.parseFromString(_xml, "text/xml");
		return xmlDoc;
	}
}

function findRootNode(_xmlRoot)
{
	for(var i=0; i<_xmlRoot.childNodes.length; i++)
	{
		if(_xmlRoot.childNodes[i].nodeType == 1) return _xmlRoot.childNodes[i];
	}
	
	return _xmlRoot;
}
