import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class homework
{
	public static void main(String [] args)
	{
		Game g = new Game();
		try
		{
			g.initialise();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found, closing program");
			System.out.println(e);
			System.exit(0);
		}
		
		try
		{
			g.run();
		}
		catch(IOException e)
		{
			System.out.println("IO Exception has occured, closing program");
			System.exit(0);
		}
	}
}

class Game
{
	private Node root;
	private float time;
	private char mode;
	private char colour;
	private char opp;
	public final static Coordinate bcorner = new Coordinate(0,0);
	public final static Coordinate wcorner = new Coordinate(15,15);
	private long startTime;

	public Game()
	{
		root = new Node();
		time = 0.0f;
		mode = ' ';
		colour = ' ';
	}

	//Getters and Setters
	public char getMode()
	{
		return mode;
	}

	public char getColour()
	{
		return colour;
	}

	public char getOpp()
	{
		return opp;
	}

	public float getTime()
	{
		return time;
	}

	public Node getRoot()
	{
		return root;
	}

	public void setMode(char m)
	{
		mode = m;
	}

	public void setColour(char c)
	{
		colour = c;
	}

	public void setOpp(char c)
	{
		opp = c;
	}

	public void setTime(float t)
	{
		time = t;
	}

	public void setRoot(Node r)
	{
		root = r;
	}

	//Functions
	public void initialise() throws FileNotFoundException
	{
		startTime = System.currentTimeMillis();

		FileInputStream fis = new FileInputStream("./input.txt");

		BufferedReader in = new BufferedReader(new InputStreamReader(fis));
		String str = "";

		try
		{
			str = in.readLine();
		}
		catch(IOException e)
		{
			System.out.println("Error reading input file, closing program");
			System.exit(0);
		}
		if(str.equals("SINGLE"))
		{
			mode = 'S';
		}
		else if(str.equals("GAME"))
		{
			mode = 'G';
		}
		else
		{
			System.out.println("Invalid mode, closing program");
			System.exit(0);
		}

		try
		{
			str = in.readLine();
		}
		catch(IOException e)
		{
			System.out.println("Error reading input file, closing program");
			System.exit(0);
		}
		if(str.equals("BLACK"))
		{
			colour = 'B';
			opp = 'W';
		}
		else if(str.equals("WHITE"))
		{
			colour = 'W';
			opp = 'B';
		}
		else
		{
			System.out.println("Invalid colour, closing program");
			System.exit(0);
		}

		try
		{
			str = in.readLine();
		}
		catch(IOException e)
		{
			System.out.println("Error reading input file, closing program");
			System.exit(0);
		}
		time = Float.parseFloat(str);
		if(time < 0.0f)
		{
			System.out.println("Invalid time, closing program");
			System.exit(0);
		}

		for(int i = 0; i < 16; ++i)
		{
			try
			{
				str = in.readLine();
			}
			catch(IOException e)
			{
				System.out.println("Error reading input file, closing program");
				System.exit(0);
			}
			if(str.length() < 16)
			{
				System.out.println("Invalid board setup, closing program");
				System.exit(0);
			}
			for(int j = 0; j < 16; ++j)
			{
				root.getState().setSpot(str.charAt(j),j,i);
			}
		}
		root.setCost(evalState(root,colour));
	}

	public void display()
	{
		System.out.println("Mode: " + mode);
		System.out.println("Player Colour: " + colour);
		System.out.println("Time: " + time);
		System.out.println("Colour win:" + isWin(root.getState(),colour));
		System.out.println("In Camp:" + inCamp(root.getState(),colour));
		System.out.println("Eval:" + root.getCost());
		System.out.println("Eval opp:" + evalState(root,'B'));
		System.out.println("Board: ");
		root.getState().display();
	}

	private long calcTime(long st)
	{
		long end = System.currentTimeMillis();
		return (end - st) / 1000;
	}

	public void run() throws IOException
	{
		if(mode == 'S')
		{
			expand(root,colour);
			LinkedList<Node> ch = root.getChildren();
			LinkedList<Node> temp;
			LinkedList<Node> temp2;
			LinkedList<Node> temp3;
			String str;
			boolean test = false;
			int num = ch.size();
			//System.out.println(calcTime(startTime));
			if(calcTime(startTime) < time - 10.0)
			{	
				for(int i = 0; i < ch.size(); ++i)
				{
					expand(ch.get(i),opp);
					if(calcTime(startTime) > time - 10.0)
					{
						test = true;
						break;
					}
				}
				//System.out.println("depth 2");
				if(!test && num < 1000)
				{
					for(int i = 0; i < ch.size(); ++i)
					{
						temp = ch.get(i).getChildren();
						if(test)
						{
							break;
						}
						num += temp.size();
						for(int j = 0; j < temp.size(); ++j)
						{
							expand(temp.get(j),colour);
							if(calcTime(startTime) > time - 10.0)
							{
								test = true;
								break;
							}
						}
					}
					//System.out.println("depth 3");
				}
				if(!test && num < 1000)
				{
					for(int i = 0; i < ch.size(); ++i)
					{
						temp = ch.get(i).getChildren();
						if(test)
						{
							break;
						}
						for(int j = 0; j < temp.size(); ++j)
						{
							if(test)
							{
								break;
							}
							temp2 = temp.get(j).getChildren();
							num += temp2.size();
							for(int k = 0; k < temp2.size(); ++k)
							{
								expand(temp2.get(k), opp);
								if(calcTime(startTime) > time - 10.0)
								{
									test = true;
									break;
								}
							}
						}
					}
					//System.out.println("depth 4");
				}
				if(!test && num < 1000)
				{
					for(int i = 0; i < ch.size(); ++i)
					{
						temp = ch.get(i).getChildren();
						if(test)
						{
							break;
						}
						for(int j = 0; j < temp.size(); ++j)
						{
							if(test)
							{
								break;
							}
							temp2 = temp.get(j).getChildren();
							for(int k = 0; k < temp2.size(); ++k)
							{
								if(test)
								{
									break;
								}
								temp3 = temp2.get(k).getChildren();
								num += temp3.size();
								for(int l = 0; l < temp3.size(); ++l)
								{
									expand(temp3.get(l),colour);
									if(calcTime(startTime) > time - 10.0)
									{
										test = true;
										break;
									}
								}
							}
						}
					}
					//System.out.println("depth 5");
				}
			}
			str = ABSearch(root);
			System.out.println(str); //here
			writeToFile(str);
		}
		else if(mode == 'G')
		{
			expand(root,colour);
			LinkedList<Node> ch = root.getChildren();
			LinkedList<Node> temp;
			LinkedList<Node> temp2;
			LinkedList<Node> temp3;
			String str;
			boolean test = false;
			int num = ch.size();
			if(calcTime(startTime) < time - 10.0)
			{	
				for(int i = 0; i < ch.size(); ++i)
				{
					expand(ch.get(i),opp);
					if(calcTime(startTime) > 4.0)
					{
						test = true;
						break;
					}
				}
				//System.out.println("depth 2");
				if(!test && num < 1000)
				{
					for(int i = 0; i < ch.size(); ++i)
					{
						temp = ch.get(i).getChildren();
						if(test)
						{
							break;
						}
						num += temp.size();
						for(int j = 0; j < temp.size(); ++j)
						{
							expand(temp.get(j),colour);
							if(calcTime(startTime) > 4.0)
							{
								test = true;
								break;
							}
						}
					}
					//System.out.println("depth 3");
				}
				if(!test && num < 1000)
				{
					for(int i = 0; i < ch.size(); ++i)
					{
						temp = ch.get(i).getChildren();
						if(test)
						{
							break;
						}
						for(int j = 0; j < temp.size(); ++j)
						{
							if(test)
							{
								break;
							}
							temp2 = temp.get(j).getChildren();
							num += temp2.size();
							for(int k = 0; k < temp2.size(); ++k)
							{
								expand(temp2.get(k), opp);
								if(calcTime(startTime) > 4.0)
								{
									test = true;
									break;
								}
							}
						}
					}
					//System.out.println("depth 4");
				}
				if(!test && num < 1000)
				{
					for(int i = 0; i < ch.size(); ++i)
					{
						temp = ch.get(i).getChildren();
						if(test)
						{
							break;
						}
						for(int j = 0; j < temp.size(); ++j)
						{
							if(test)
							{
								break;
							}
							temp2 = temp.get(j).getChildren();
							for(int k = 0; k < temp2.size(); ++k)
							{
								if(test)
								{
									break;
								}
								temp3 = temp2.get(k).getChildren();
								num += temp3.size();
								for(int l = 0; l < temp3.size(); ++l)
								{
									expand(temp3.get(l),colour);
									if(calcTime(startTime) > 4.0)
									{
										test = true;
										break;
									}
								}
							}
						}
					}
					//System.out.println("depth 5");
				}
			}
			str = ABSearch(root);
			System.out.println(str); //here
			writeToFile(str);
		}
		//System.out.println(calcTime(startTime));
	}

	private void writeToFile(String s) throws IOException
	{
		PrintWriter writer = new PrintWriter(new FileWriter("output.txt"));
		writer.println(s);
		writer.close();
	}

	public String ABSearch(Node n)
	{
		Node res = maxVal(n,-500,500);
		//System.out.println(res.getCost());
		while(res.getParent() != n)
		{
			res = res.getParent();
		}
		//res.getState().display();
		return getMove(res);
	}

	public Node maxVal(Node n, int a, int b)
	{
		LinkedList<Node> ch = n.getChildren();
		int val = 0;
		Node res = n;
		Node temp;
		if(ch.size() == 0)
		{
			return n;
		}
		int v = -500;
		for(int i = 0; i < ch.size(); ++i)
		{
			temp = minVal(ch.get(i),a,b);
			val = temp.getCost();
			v = Math.max(v, val);
			if(v == val)
			{
				res = temp;
			}
			if(v >= b)
			{
				return ch.get(i);
			}
			a = Math.max(a,v);
		}
		return res;
	}

	public Node minVal(Node n, int a, int b)
	{
		LinkedList<Node> ch = n.getChildren();
		int val = 0;
		Node res = n;
		Node temp;
		if(ch.size() == 0)
		{
			return n;
		}
		int v = 500;
		for(int i = 0; i < ch.size(); ++i)
		{
			temp = maxVal(ch.get(i),a,b);
			val = temp.getCost();
			v = Math.min(v, val);
			if(v == val)
			{
				res = temp;
			}
			if(v <= a)
			{
				return ch.get(i);
			}
			b = Math.min(b,v);
		}
		return res;
	}

	private LinkedList<Coordinate> copySpots(LinkedList<Coordinate> s)
	{
		LinkedList<Coordinate> temp = new LinkedList<Coordinate>();
		for(int i = 0; i < s.size(); ++i)
		{
			temp.add(new Coordinate(s.get(i).getX(), s.get(i).getY()));
		}
		return temp;
	}

	private Node expand(Node p, char clr)
	{
		//Expand current state to possible valid states
		Queue<Node> q = new LinkedList<>();
		LinkedList<Coordinate> spots;
		LinkedList<Coordinate> jmps;
		LinkedList<Coordinate> pieces = null;
		Board b = new Board(p.getState().getBoard());
		LinkedList<Coordinate> home;
		boolean test = true;
		Node nd;
		int st = 0;
		if(clr == 'W')
		{
			pieces = p.getState().getWhite();
			home = Board.whome;
		}
		else
		{
			pieces = p.getState().getBlack();
			home = Board.bhome;
		}
		if(inCamp(b,clr))
		{
			pieces = p.getState().getInCamp(clr);
			//System.out.println("in camp " + pieces.size());
		}
		for(int i = 0; i < pieces.size(); ++i)
		{
			spots = p.getState().getEmpty(pieces.get(i));
			jmps = p.getState().getJumpSpots(pieces.get(i), p.getJumps());
			LinkedList<Coordinate> temp = new LinkedList<Coordinate>();
			if(inCamp(b,clr))
			{
				for(int j = 0; j < spots.size(); ++j)
				{
					if(!home.contains(spots.get(j)))
					{
						temp.add(new Coordinate(spots.get(j).getX(),spots.get(j).getY()));
					}
				}
				if(temp.size() == 0 && spots.size() > 0)
				{
					test = false;
				}
				else if(temp.size() > 0)
				{
					//spots = temp;
					spots = copySpots(temp);
					test = false;
				}
				temp.clear();
				
				for(int j = 0; j < jmps.size(); ++j)
				{
					if(!home.contains(jmps.get(j)))
					{
						temp.add(new Coordinate(jmps.get(j).getX(),jmps.get(j).getY()));
					}
				}
				if(temp.size() > 0)
				{	
					test = false;
					for(int j = 0; j < temp.size(); ++j)
					{
						spots.add(temp.get(j));
					}	
				}
				else if(temp.size() == 0 && jmps.size() > 0)
				{
					test = false;
					for(int j = 0; j < jmps.size(); ++j)
					{
						spots.add(jmps.get(j));
					}
				}
			}
			else
			{
				for(int j = 0; j < jmps.size(); ++j)
				{
					spots.add(jmps.get(j));
				}
			}
			//System.out.println("Spots size:" + spots.size());
			for(int j = 0; j < spots.size(); ++j)
			{
				nd = new Node();
				nd.setParent(p);
				nd.setState(p.getState());
				nd.getState().move(pieces.get(i).getX(),pieces.get(i).getY(),spots.get(j).getX(),spots.get(j).getY());
				nd.addJump(pieces.get(i));
				nd.addJump(spots.get(j));
				nd.setCost(evalState(nd,colour));
				p.addChild(nd);
			}
			spots.clear();
		}
		if(inCamp(b,clr) && test)
		{
			//System.out.println("in if");
			LinkedList<Coordinate> temp2 = pieces;
			if(clr == 'W')
			{
				pieces = p.getState().getWhite();
				home = Board.whome;
			}
			else
			{
				pieces = p.getState().getBlack();
				home = Board.bhome;
			}
			for(int i = 0; i < pieces.size(); ++i)
			{
				if(!temp2.contains(pieces.get(i)))
				{
					spots = p.getState().getEmpty(pieces.get(i));
					jmps = p.getState().getJumpSpots(pieces.get(i), p.getJumps());
					for(int j = 0; j < spots.size(); ++j)
					{
						nd = new Node();
						nd.setParent(p);
						nd.setState(p.getState());
						nd.getState().move(pieces.get(i).getX(),pieces.get(i).getY(),spots.get(j).getX(),spots.get(j).getY());
						nd.addJump(pieces.get(i));
						nd.addJump(spots.get(j));
						nd.setCost(evalState(nd,colour));
						p.addChild(nd);
					}
					//System.out.println("Jumps size:" + jmps.size());
					for(int j = 0; j < jmps.size(); ++j)
					{
						nd = new Node();
						nd.setParent(p);
						nd.setState(p.getState());
						nd.getState().move(pieces.get(i).getX(),pieces.get(i).getY(),jmps.get(j).getX(),jmps.get(j).getY());
						nd.addJump(pieces.get(i));
						nd.addJump(jmps.get(j));
						nd.setCost(evalState(nd,colour));
						p.addChild(nd);
					}
				}
			}
		}
		expandJumps(p,p.getChildren(),clr);
		return p;
	}

	private void expandJumps(Node p, LinkedList<Node> ch, char clr)
	{
		LinkedList<Coordinate> jmps;
		LinkedList<Coordinate> temp;
		Coordinate coord;
		Coordinate start;
		Node nd;
		LinkedList<Node> temp2 = new LinkedList<Node>();
		for(int i = 0; i < ch.size(); ++i)
		{
			temp = ch.get(i).getJumps();
			coord = temp.get(temp.size()-1);
			start = temp.get(0);
			int xp = start.getX();
			int yp = start.getY();
			int xc = coord.getX();
			int yc = coord.getY();
			if(Math.abs(xp-xc) > 1 || Math.abs(yp-yc) > 1)
			{
				jmps = ch.get(i).getState().getJumpSpots(coord, ch.get(i).getJumps());
				for(int j = 0; j < jmps.size(); ++j)
				{
					coord = temp.get(0);
					if(!ch.get(i).getJumps().contains(jmps.get(j)))
					{
						nd = new Node();
						nd.setParent(p);
						nd.setState(p.getState());
						nd.getState().move(coord.getX(),coord.getY(),jmps.get(j).getX(),jmps.get(j).getY());
						nd.setJumps(ch.get(i).getJumps());
						nd.addJump(jmps.get(j));
						nd.setCost(evalState(nd,colour));
						p.addChild(nd);
						temp2.add(nd);
					}
				}
			}
		}
		if(temp2.size() > 0)
		{
			expandJumps(p, temp2, clr);
		}
	}

	private boolean inCamp(Board board, char clr)
	{
		//Check if there are pieces in the camp or not
		boolean val = false;
		if(clr == 'W')
		{
			if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-1) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-2) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-3) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-4) == 'W')
			{
				val = true;
				return val;
			}	
			if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-5) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-1) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-2) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-3) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-4) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-5) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-1) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-2) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-3) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-4) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-4,Board.SIZE_X-1) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-4,Board.SIZE_X-2) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-4,Board.SIZE_X-3) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-5,Board.SIZE_X-1) == 'W')
			{
				val = true;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-5,Board.SIZE_X-2) == 'W')
			{
				val = true;
				return val;
			}
		}
		else if(clr == 'B')
		{
			if(board.getSpot(0,0) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(0,1) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(0,2) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(0,3) == 'B')
			{
				val = true;
				return val;
			}	
			if(board.getSpot(0,4) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(1,0) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(1,1) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(1,2) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(1,3) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(1,4) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(2,0) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(2,1) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(2,2) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(2,3) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(3,0) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(3,1) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(3,2) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(4,0) == 'B')
			{
				val = true;
				return val;
			}
			if(board.getSpot(4,1) == 'B')
			{
				val = true;
				return val;
			}
		}
		return val;
	}

	private boolean isWin(Board board, char clr)
	{
		//Check if it is win condition
		boolean val = true;
		if(isInitState(board))
		{
			return false;
		}
		if(clr == 'W')
		{
			if(board.getSpot(0,0) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(0,1) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(0,2) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(0,3) == '.')
			{
				val = false;
				return val;
			}	
			if(board.getSpot(0,4) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(1,0) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(1,1) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(1,2) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(1,3) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(1,4) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(2,0) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(2,1) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(2,2) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(2,3) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(3,0) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(3,1) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(3,2) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(4,0) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(4,1) == '.')
			{
				val = false;
				return val;
			}
		}
		else if(clr == 'B')
		{
			if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-1) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-2) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-3) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-4) == '.')
			{
				val = false;
				return val;
			}	
			if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-5) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-1) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-2) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-3) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-4) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-5) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-1) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-2) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-3) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-4) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-4,Board.SIZE_X-1) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-4,Board.SIZE_X-2) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-4,Board.SIZE_X-3) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-5,Board.SIZE_X-1) == '.')
			{
				val = false;
				return val;
			}
			if(board.getSpot(Board.SIZE_Y-5,Board.SIZE_X-2) == '.')
			{
				val = false;
				return val;
			}
		}
		return val;
	}

	private boolean isInitState(Board board)
	{
		boolean val = true;
		if(board.getSpot(0,0) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(0,1) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(0,2) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(0,3) != 'B')
		{
			val = false;
			return val;
		}	
		if(board.getSpot(0,4) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(1,0) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(1,1) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(1,2) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(1,3) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(1,4) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(2,0) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(2,1) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(2,2) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(2,3) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(3,0) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(3,1) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(3,2) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(4,0) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(4,1) != 'B')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-1) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-2) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-3) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-4) != 'W')
		{
			val = false;
			return val;
		}	
		if(board.getSpot(Board.SIZE_Y-1,Board.SIZE_X-5) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-1) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-2) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-3) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-4) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-2,Board.SIZE_X-5) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-1) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-2) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-3) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-3,Board.SIZE_X-4) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-4,Board.SIZE_X-1) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-4,Board.SIZE_X-2) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-4,Board.SIZE_X-3) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-5,Board.SIZE_X-1) != 'W')
		{
			val = false;
			return val;
		}
		if(board.getSpot(Board.SIZE_Y-5,Board.SIZE_X-2) != 'W')
		{
			val = false;
			return val;
		}
		return val;
	}

	private int evalState(Node n, char clr)
	{
		//Have a function that gives a numeric value for the win chance of a state based on the board state
		int val = 0;
		char c;
		Board b = n.getState();
		Coordinate st = null;
		Coordinate end = null;
		LinkedList<Coordinate> jmps = n.getJumps();
		int z = 0;
		int y = 0;
		int x = 0;
		int w = 0;
		int v = 0;
		if(clr == 'W')
		{
			c = 'B';
			if(jmps.size() > 1)
			{
				st = jmps.get(0);
				end = jmps.get(jmps.size()-1);
				if(Board.bhome.contains(st) && Board.bhome.contains(end))
				{
					z = 30;
				}
				y = b.CalcDist(end, new Coordinate(0,0));
				x = b.CalcDist(st,end) - 2;
				int xp = st.getX();
				int yp = st.getY();
				int xc = jmps.get(1).getX();
				int yc = jmps.get(1).getY();
				if((Math.abs(xp-xc) == 1 || Math.abs(xp-xc) == 0) && (Math.abs(yp-yc) == 1 || Math.abs(yp-yc) == 0))
				{
					
				}
				else
				{
					v+= 5;
					w = 5;
					y += 1;
				}
				if(Board.whome.contains(st))
				{
					v += 20;
				}
			}
		}
		else
		{
			c = 'W';
			if(jmps.size() > 1)
			{
				st = jmps.get(0);
				end = jmps.get(jmps.size()-1);	
				if(Board.whome.contains(st) && Board.whome.contains(end))
				{
					z = 30;
				}
				y = b.CalcDist(end, new Coordinate(Board.SIZE_X-1,Board.SIZE_Y-1));
				x = b.CalcDist(st,end) - 2;
				int xp = st.getX();
				int yp = st.getY();
				int xc = jmps.get(1).getX();
				int yc = jmps.get(1).getY();
				if((Math.abs(xp-xc) == 1 || Math.abs(xp-xc) == 0) && (Math.abs(yp-yc) == 1 || Math.abs(yp-yc) == 0))
				{
					
				}
				else
				{
					v += 5;
					w = 5;
					y += 1;
				}
				if(Board.bhome.contains(st))
				{
					v += 20;
				}
			}
		}
		if(isWin(b,clr))
		{
			val = 400;
		}
		else if(isWin(b,c))
		{
			val = -400;
		}
		else
		{
			int numWin = b.getNumWin(clr);
			int numCamp = b.getNumCamp(clr);
			int numOut = b.getNumOut(clr);
			int numSpot = b.getNumSpot(clr);
			int dist = b.getDist(clr);
			int onw = b.getNumWin(c);
			int onc = b.getNumCamp(c);
			int ono = b.getNumOut(c);
			int ons = b.getNumSpot(c);
			int odist = b.getDist(c);
			val += 10 * numWin - 3 * (numSpot-numCamp) - 4 *(dist / (numOut + 1)) - z + 3 * y + x + w + v;
			val -= 5 * onw + 3 * (ons-onc) - 4 * (odist / (ono + 1));
			//val = 10 * numWin - 6 * (numSpot-numCamp) - z + 3 * y;
			//val /= 3 * (dist / (numOut +1));
		}
		return val;
	}

	public String getMove(Node c)
	{
		LinkedList<Coordinate> j = c.getJumps();
		String move = "";
		if(j.size() == 2)
		{
			int xp = j.get(0).getX();
			int yp = j.get(0).getY();
			int xc = j.get(1).getX();
			int yc = j.get(1).getY();
			if((Math.abs(xp-xc) == 1 || Math.abs(xp-xc) == 0) && (Math.abs(yp-yc) == 1 || Math.abs(yp-yc) == 0))
			{
				move = "E " + j.get(0) + " " + j.get(1);
			}
			else
			{
				move = "J " + j.get(0) + " " + j.get(1);
			}
		}
		else if(j.size() > 2)
		{
			for(int i = 0; i < j.size() - 2; ++i)
			{
				move += "J " + j.get(i) + " " + j.get(i+1) + "\n";
			}
			move += "J " + j.get(j.size()-2) + " " + j.get(j.size()-1);
		}
		return move;
	}
}

class Node
{
	private Board state;
	private Node parent;
	private int cost;
	private LinkedList<Node> children;
	private LinkedList<Coordinate> jumps;

	public Node()
	{
		state = new Board();
		parent = null;
		cost = 0;
		children = new LinkedList<Node>();
		jumps = new LinkedList<Coordinate>();
	}

	public Node(Board s, Node p, int c, LinkedList<Node> ch, LinkedList<Coordinate> j)
	{
		state = new Board(s.getBoard());
		parent = p;
		cost = c;
		children = ch;
		jumps = j;
	}

	//Getters and Setters
	public Board getState()
	{
		return state;
	}

	public Node getParent()
	{
		return parent;
	}

	public int getCost()
	{
		return cost;
	}

	public LinkedList<Node> getChildren()
	{
		return children;
	}

	public LinkedList<Coordinate> getJumps()
	{
		return jumps;
	}

	public void setState(Board b)
	{
		state.copyBoard(b.getBoard());
	}

	public void setParent(Node p)
	{
		parent = p;
	}

	public void setCost(int c)
	{
		cost = c;
	}

	public void setChildren(LinkedList<Node> ch)
	{
		children = ch;
	}

	public void addChild(Node c)
	{
		children.add(c);
	}

	public void setJumps(LinkedList<Coordinate> j)
	{
		jumps = j;
	}

	public void addJump(Coordinate c)
	{
		jumps.add(c);
	}
}

class Board
{
	static final int SIZE_X = 16;
	static final int SIZE_Y = 16;
	private char [][] board;
	private LinkedList<Coordinate> white;
	private LinkedList<Coordinate> black;
	static final LinkedList<Coordinate> bhome = new LinkedList<Coordinate>(){{
		add(new Coordinate(0,0));
		add(new Coordinate(1,0));
		add(new Coordinate(2,0));
		add(new Coordinate(3,0));
		add(new Coordinate(4,0));
		add(new Coordinate(0,1));
		add(new Coordinate(1,1));
		add(new Coordinate(2,1));
		add(new Coordinate(3,1));
		add(new Coordinate(4,1));
		add(new Coordinate(0,2));
		add(new Coordinate(1,2));
		add(new Coordinate(2,2));
		add(new Coordinate(3,2));
		add(new Coordinate(0,3));
		add(new Coordinate(1,3));
		add(new Coordinate(2,3));
		add(new Coordinate(0,4));
		add(new Coordinate(1,4));
	}};
	static final LinkedList<Coordinate> whome = new LinkedList<Coordinate>(){{
		add(new Coordinate(SIZE_X-1,SIZE_Y-1));
		add(new Coordinate(SIZE_X-2,SIZE_Y-1));
		add(new Coordinate(SIZE_X-3,SIZE_Y-1));
		add(new Coordinate(SIZE_X-4,SIZE_Y-1));
		add(new Coordinate(SIZE_X-5,SIZE_Y-1));
		add(new Coordinate(SIZE_X-1,SIZE_Y-2));
		add(new Coordinate(SIZE_X-2,SIZE_Y-2));
		add(new Coordinate(SIZE_X-3,SIZE_Y-2));
		add(new Coordinate(SIZE_X-4,SIZE_Y-2));
		add(new Coordinate(SIZE_X-5,SIZE_Y-2));
		add(new Coordinate(SIZE_X-1,SIZE_Y-3));
		add(new Coordinate(SIZE_X-2,SIZE_Y-3));
		add(new Coordinate(SIZE_X-3,SIZE_Y-3));
		add(new Coordinate(SIZE_X-4,SIZE_Y-3));
		add(new Coordinate(SIZE_X-1,SIZE_Y-4));
		add(new Coordinate(SIZE_X-2,SIZE_Y-4));
		add(new Coordinate(SIZE_X-3,SIZE_Y-4));
		add(new Coordinate(SIZE_X-1,SIZE_Y-5));
		add(new Coordinate(SIZE_X-2,SIZE_Y-5));
	}};

	public Board()
	{
		board = new char[SIZE_Y][SIZE_X];
		white = new LinkedList<Coordinate>();
		black = new LinkedList<Coordinate>();
	}

	public Board(char [][] b)
	{
		board = new char[SIZE_Y][SIZE_X];
		white = new LinkedList<Coordinate>();
		black = new LinkedList<Coordinate>();
		for(int i = 0; i < SIZE_Y; ++i)
		{
			for(int j = 0; j < SIZE_X; ++j)
			{
				board[i][j] = b[i][j];
				if(board[i][j] == 'W')
				{
					white.add(new Coordinate(j,i));
				}
				if(board[i][j] == 'B')
				{
					black.add(new Coordinate(j,i));
				}
			}
		}
	}

	//Getters and Setters
	public char[][] getBoard()
	{
		return board;
	}

	public char getSpot(int y, int x)
	{
		return board[y][x];
	}

	public LinkedList<Coordinate> getWhite()
	{
		return white;
	}

	public LinkedList<Coordinate> getBlack()
	{
		return black;
	}

	public void setBoard(char [][] b)
	{
		board = b;
	}

	public void setSpot(char c, int x, int y)
	{
		if(c == 'W')
		{
			white.add(new Coordinate(x,y));
		}
		else if(c == 'B')
		{
			black.add(new Coordinate(x,y));
		}
		board[y][x] = c;
	}

	//Functions
	public void copyBoard(char [][] b)
	{
		white = new LinkedList<Coordinate>();
		black = new LinkedList<Coordinate>();
		for(int i = 0; i < SIZE_Y; ++i)
		{
			for(int j = 0; j < SIZE_X; ++j)
			{
				board[i][j] = b[i][j];
				if(board[i][j] == 'W')
				{
					white.add(new Coordinate(j,i));
				}
				if(board[i][j] == 'B')
				{
					black.add(new Coordinate(j,i));
				}
			}
		}
	}

	public LinkedList<Coordinate> getInCamp(char clr)
	{
		LinkedList<Coordinate> pieces;
		LinkedList<Coordinate> home;
		LinkedList<Coordinate> res = new LinkedList<Coordinate>();
		if(clr == 'W')
		{
			pieces = white;
			home = whome;
		}
		else
		{
			pieces = black;
			home = bhome;
		}
		Coordinate coord;
		for(int i = 0; i < pieces.size(); ++i)
		{
			coord = pieces.get(i);
			if(home.contains(coord))
			{
				res.add(new Coordinate(coord.getX(),coord.getY()));
			}
		}
		return res;
	}

	public LinkedList<Coordinate> getEmpty(Coordinate c)
	{
		int x = c.getX();
		int y = c.getY();
		LinkedList<Coordinate> home;
		LinkedList<Coordinate> opp;
		Coordinate coord;
		if(board[y][x] == 'W')
		{
			home = whome;
			opp = bhome;
		}
		else
		{
			home = bhome;
			opp = whome;
		}

		LinkedList<Coordinate> res = new LinkedList<Coordinate>();
		
		if(home.contains(c))
		{
			if(board[y][x] == 'W')
			{
				if(y - 1 > -1)
				{
					coord = new Coordinate(x,y-1);
					if(board[y-1][x] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}
				}
				if(x - 1 > -1)
				{
					coord = new Coordinate(x-1,y);
					if(board[y][x-1] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}
				}
				if(x - 1 > -1 && y - 1 > -1)
				{
					coord = new Coordinate(x-1,y-1);
					if(board[y-1][x-1] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}
				}
			}
			else
			{
				if(x + 1 < SIZE_X)
				{
					coord = new Coordinate(x+1,y);
					if(board[y][x+1] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}	
				}
				if(x + 1 < SIZE_X && y + 1 < SIZE_Y)
				{
					coord = new Coordinate(x+1,y+1);
					if(board[y+1][x+1] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}
				}
				if(y + 1 < SIZE_Y)
				{
					coord = new Coordinate(x,y+1);
					if(board[y+1][x] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}
				}
			}
		}
		else
		{
			if(y - 1 > -1)
			{
				coord = new Coordinate(x,y-1);
				if(board[y-1][x] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
				{
					res.add(coord);
				}
			}
			if(x + 1 < SIZE_X && y - 1 > -1)
			{
				coord = new Coordinate(x+1,y-1);
				if(board[y-1][x+1] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
				{
					res.add(coord);
				}
			}
			if(x + 1 < SIZE_X)
			{
				coord = new Coordinate(x+1,y);
				if(board[y][x+1] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
				{
					res.add(coord);
				}	
			}
			if(x + 1 < SIZE_X && y + 1 < SIZE_Y)
			{
				coord = new Coordinate(x+1,y+1);
				if(board[y+1][x+1] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
				{
					res.add(coord);
				}
			}
			if(y + 1 < SIZE_Y)
			{
				coord = new Coordinate(x,y+1);
				if(board[y+1][x] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
				{
					res.add(coord);
				}
			}
			if(x - 1 > -1 && y + 1 < SIZE_Y)
			{
				coord = new Coordinate(x-1,y+1);
				if(board[y+1][x-1] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
				{
					res.add(coord);
				}
			}
			if(x - 1 > -1)
			{
				coord = new Coordinate(x-1,y);
				if(board[y][x-1] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
				{
					res.add(coord);
				}
			}
			if(x - 1 > -1 && y - 1 > -1)
			{
				coord = new Coordinate(x-1,y-1);
				if(board[y-1][x-1] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
				{
					res.add(coord);
				}
			}
		}
		return res;
	}

	public LinkedList<Coordinate> getJumpSpots(Coordinate c, LinkedList<Coordinate> jmp)
	{
		int x = c.getX();
		int y = c.getY();
		LinkedList<Coordinate> home;
		LinkedList<Coordinate> opp;
		Coordinate coord;
		if(board[y][x] == 'W')
		{
			home = whome;
			opp = bhome;
		}
		else
		{
			home = bhome;
			opp = whome;
		}

		LinkedList<Coordinate> res = new LinkedList<Coordinate>();

		if(board[y][x] == 'W')
		{
			if(y - 1 > -1 && y - 2 > -1)
			{
				coord = new Coordinate(x,y-2);
				if(!jmp.contains(coord))
				{	
					if(board[y-1][x] != '.' && board[y-2][x] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}
				}
			}
			if(x - 1 > -1 && x - 2 > -1)
			{
				coord = new Coordinate(x-2,y);
				if(!jmp.contains(coord))
				{
					if(board[y][x-1] != '.' && board[y][x-2] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}
				}
			}
			if(x - 1 > -1 && y - 1 > -1 && x -2 > -1 && y - 2 > -1)
			{
				coord = new Coordinate(x-2,y-2);
				if(!jmp.contains(coord))
				{
					if(board[y-1][x-1] != '.' && board[y-2][x-2] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}
				}
			}
		}
		else
		{
			if(x + 1 < SIZE_X && x + 2 < SIZE_X)
			{
				coord = new Coordinate(x+2,y);
				if(!jmp.contains(coord))
				{
					if(board[y][x+1] != '.' && board[y][x+2] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}
				}	
			}
			if(x + 1 < SIZE_X && y + 1 < SIZE_Y && x + 2 < SIZE_X && y + 2 < SIZE_Y)
			{
				coord = new Coordinate(x+2,y+2);
				if(!jmp.contains(coord))
				{
					if(board[y+1][x+1] != '.' && board[y+2][x+2] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}
				}
			}
			if(y + 1 < SIZE_Y && y + 2 < SIZE_Y)
			{
				coord = new Coordinate(x,y+2);
				if(!jmp.contains(coord))
				{
					if(board[y+1][x] != '.' && board[y+2][x] == '.' && !(!home.contains(c) && home.contains(coord)) && !(opp.contains(c) && !opp.contains(coord)))
					{
						res.add(coord);
					}
				}
			}
		}
		return res;		
	}

	public int getNumWin(char clr)
	{
		int val = 0;
		if(clr == 'W')
		{
			for(int i = 0; i < white.size(); ++i)
			{
				if(bhome.contains(white.get(i)))
				{
					++val;
				}
			}
		}
		else
		{
			for(int i = 0; i < black.size(); ++i)
			{
				if(whome.contains(black.get(i)))
				{
					++val;
				}
			}
		}
		return val;
	}

	public int getNumOut(char clr)
	{
		int val = 0;
		if(clr == 'W')
		{
			for(int i = 0; i < white.size(); ++i)
			{
				if(!whome.contains(white.get(i)) && !bhome.contains(white.get(i)))
				{
					++val;
				}
			}
		}
		else
		{
			for(int i = 0; i < black.size(); ++i)
			{
				if(!whome.contains(black.get(i)) && !bhome.contains(black.get(i)))
				{
					++val;
				}
			}
		}
		return val;
	}

	public int getNumSpot(char clr)
	{
		int val = 0;
		if(clr == 'W')
		{
			val = bhome.size() - getNumWin(clr) - getNumCamp('B');
		}
		else
		{
			val = whome.size() - getNumWin(clr) - getNumCamp('W');
		}
		return val;
	}

	public int getNumCamp(char clr)
	{
		int val = 0;
		if(clr == 'W')
		{
			for(int i = 0; i < white.size(); ++i)
			{
				if(whome.contains(white.get(i)))
				{
					++val;
				}
			}
		}
		else
		{
			for(int i = 0; i < black.size(); ++i)
			{
				if(bhome.contains(black.get(i)))
				{
					++val;
				}
			}
		}
		return val;
	}

	public int CalcDist(Coordinate x, Coordinate y)
	{
		int x1 = x.getX();
		int y1 = x.getY();
		int x2 = y.getX();
		int y2 = y.getY();
		int val = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
		return val;
	}

	public int getDist(char clr)
	{
		int val = 0;
		if(clr == 'W')
		{
			for(int i = 0; i < white.size(); ++i)
			{
				if(!bhome.contains(white.get(i)))
				{	
					val += CalcDist(white.get(i), new Coordinate(0,0));
				}
			}
		}
		else
		{
			for(int i = 0; i < black.size(); ++i)
			{
				if(!whome.contains(black.get(i)))
				{
					val += CalcDist(black.get(i), new Coordinate(SIZE_X-1,SIZE_Y-1));
				}
			}
		}
		return val;
	}	

	/*public void initialiseBoard()
	{
		for(int i = 0; i < SIZE_Y; ++i)
		{
			for(int j = 0; j < SIZE_X; ++j)
			{
				board[i][j] = '.';
			}
		}

		black.clear();
		white.clear();

		board[0][0] = 'B';
		black.add(new Coordinate(0,0));
		board[0][1] = 'B';
		black.add(new Coordinate(1,0));
		board[0][2] = 'B';
		black.add(new Coordinate(2,0));
		board[0][3] = 'B';
		black.add(new Coordinate(3,0));
		board[0][4] = 'B';
		black.add(new Coordinate(4,0));
		board[1][0] = 'B';
		black.add(new Coordinate(0,1));
		board[1][1] = 'B';
		black.add(new Coordinate(1,1));
		board[1][2] = 'B';
		black.add(new Coordinate(2,1));
		board[1][3] = 'B';
		black.add(new Coordinate(3,1));
		board[1][4] = 'B';
		black.add(new Coordinate(4,1));
		board[2][0] = 'B';
		black.add(new Coordinate(0,2));
		board[2][1] = 'B';
		black.add(new Coordinate(1,2));
		board[2][2] = 'B';
		black.add(new Coordinate(2,2));
		board[2][3] = 'B';
		black.add(new Coordinate(3,2));
		board[3][0] = 'B';
		black.add(new Coordinate(0,3));
		board[3][1] = 'B';
		black.add(new Coordinate(1,3));
		board[3][2] = 'B';
		black.add(new Coordinate(2,3));
		board[4][0] = 'B';
		black.add(new Coordinate(0,4));
		board[4][1] = 'B';
		black.add(new Coordinate(1,4));
		
		board[SIZE_Y-1][SIZE_X-1] = 'W';
		white.add(new Coordinate(SIZE_X-1,SIZE_Y-1));
		board[SIZE_Y-1][SIZE_X-2] = 'W';
		white.add(new Coordinate(SIZE_X-2,SIZE_Y-1));
		board[SIZE_Y-1][SIZE_X-3] = 'W';
		white.add(new Coordinate(SIZE_X-3,SIZE_Y-1));
		board[SIZE_Y-1][SIZE_X-4] = 'W';
		white.add(new Coordinate(SIZE_X-4,SIZE_Y-1));
		board[SIZE_Y-1][SIZE_X-5] = 'W';
		white.add(new Coordinate(SIZE_X-5,SIZE_Y-1));
		board[SIZE_Y-2][SIZE_X-1] = 'W';
		white.add(new Coordinate(SIZE_X-1,SIZE_Y-2));
		board[SIZE_Y-2][SIZE_X-2] = 'W';
		white.add(new Coordinate(SIZE_X-2,SIZE_Y-2));
		board[SIZE_Y-2][SIZE_X-3] = 'W';
		white.add(new Coordinate(SIZE_X-3,SIZE_Y-2));
		board[SIZE_Y-2][SIZE_X-4] = 'W';
		white.add(new Coordinate(SIZE_X-4,SIZE_Y-2));
		board[SIZE_Y-2][SIZE_X-5] = 'W';
		white.add(new Coordinate(SIZE_X-5,SIZE_Y-2));
		board[SIZE_Y-3][SIZE_X-1] = 'W';
		white.add(new Coordinate(SIZE_X-1,SIZE_Y-3));
		board[SIZE_Y-3][SIZE_X-2] = 'W';
		white.add(new Coordinate(SIZE_X-2,SIZE_Y-3));
		board[SIZE_Y-3][SIZE_X-3] = 'W';
		white.add(new Coordinate(SIZE_X-3,SIZE_Y-3));
		board[SIZE_Y-3][SIZE_X-4] = 'W';
		white.add(new Coordinate(SIZE_X-4,SIZE_Y-3));
		board[SIZE_Y-4][SIZE_X-1] = 'W';
		white.add(new Coordinate(SIZE_X-1,SIZE_Y-4));
		board[SIZE_Y-4][SIZE_X-2] = 'W';
		white.add(new Coordinate(SIZE_X-2,SIZE_Y-4));
		board[SIZE_Y-4][SIZE_X-3] = 'W';
		white.add(new Coordinate(SIZE_X-2,SIZE_Y-4));
		board[SIZE_Y-5][SIZE_X-1] = 'W';
		white.add(new Coordinate(SIZE_X-1,SIZE_Y-5));
		board[SIZE_Y-5][SIZE_X-2] = 'W';
		white.add(new Coordinate(SIZE_X-2,SIZE_Y-5));
	}*/

	public void move(int init_x, int init_y, int fin_x, int fin_y)
	{
		if(init_y < 0 || init_y >= SIZE_Y || init_x < 0 || init_x >= SIZE_X)
		{
			System.out.println("Invalid start spot");
			return;	
		}
		if(fin_y < 0 || fin_y >= SIZE_Y || fin_x < 0 || fin_x >= SIZE_X)
		{
			System.out.println("Invalid end spot");
			return;	
		}

		char temp = board[init_y][init_x];
		
		if(temp == '.')
		{
			System.out.println("Trying to move an empty spot");
			return;
		}
		if(board[fin_y][fin_x] != '.')
		{
			System.out.println("Invalid move");
			System.out.println("init: " + init_x + "," + init_y + " end: " + fin_x + "," + fin_y);
			return;
		}
		
		if(board[init_y][init_x] == 'W')
		{
			white.remove(new Coordinate(init_x,init_y));
			white.add(new Coordinate(fin_x,fin_y));
		}
		else if(board[init_y][init_x] == 'B')
		{
			black.remove(new Coordinate(init_x,init_y));
			black.add(new Coordinate(fin_x,fin_y));
		}

		board[init_y][init_x] = '.';
		board[fin_y][fin_x] = temp;

	}

	public void display()
	{
		for(int i = 0; i < SIZE_Y; ++i)
		{
			for(int j = 0; j < SIZE_X; ++j)
			{
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}

		/*System.out.print("White Home:");
		for(int i = 0; i < whome.size(); ++i)
		{
			System.out.print(whome.get(i) + " ");
		}
		System.out.println();
		System.out.print("Black Home:");
		for(int i = 0; i < bhome.size(); ++i)
		{
			System.out.print(bhome.get(i) + " ");
		}
		System.out.println();*/
	}
}

class Coordinate
{
	int x;
	int y;

	public Coordinate(int xp, int yp)
	{
		x = xp;
		y = yp;
	}

	public Coordinate()
	{

	}

	//Getters and Setters
	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public void setX(int xp)
	{
		x = xp;
	}

	public void setY(int yp)
	{
		y = yp;
	}

	@Override
	public String toString()
	{
		return x + "," + y;
	}

	@Override
	public boolean equals(Object o)
	{
		if(o == this)
		{
			return true;
		}

		if(!(o instanceof Coordinate))
		{
			return false;
		}

		Coordinate t = (Coordinate) o;

		return Integer.compare(x, t.getX()) == 0 && Integer.compare(y, t.getY()) == 0;
	}
}