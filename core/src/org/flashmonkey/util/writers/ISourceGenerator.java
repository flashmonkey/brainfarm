package org.flashmonkey.util.writers;

import javax.xml.transform.Source;

import org.flashmonkey.neat.run.IEvolution;

public interface ISourceGenerator {

	Source getSource(IEvolution evolution);
}
