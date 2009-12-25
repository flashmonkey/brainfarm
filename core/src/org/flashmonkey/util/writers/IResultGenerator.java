package org.flashmonkey.util.writers;

import javax.xml.transform.Result;

import org.flashmonkey.neat.run.IEvolution;

public interface IResultGenerator {

	Result getResult(IEvolution evolution);
}
