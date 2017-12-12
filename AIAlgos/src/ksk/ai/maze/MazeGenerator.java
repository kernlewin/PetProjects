package ksk.ai.maze;

/*
 * Interface representing strategies for building a maze.
 * Contains one method that takes a Grid, start location and end location, and turns the
 * grid into a maze.  The only promise made is that a path will exist between start and end.
 */

public interface MazeGenerator {

	public void generate(Grid g, GridNode start, GridNode goal);
}
