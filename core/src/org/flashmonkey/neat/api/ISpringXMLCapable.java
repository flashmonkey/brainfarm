package org.flashmonkey.neat.api;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public interface ISpringXMLCapable {
	
	Node toSpringXML(Document xml);
}
