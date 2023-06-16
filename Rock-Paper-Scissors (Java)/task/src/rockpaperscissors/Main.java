package rockpaperscissors;

import java.io.*;
import java.util.*;

public class Main {
	private static final String FILEPATH = "rating.txt";
	private static int counter = 0;
	private static int i = 0;
	private static StringBuilder[] words;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Random random = new Random();
		ArrayList<String> elements = createArrayList("rock", "paper", "scissors");
		ArrayList<String> fiveElements = createArrayList("rock", "paper", "scissors", "lizard", "spock");
		ArrayList<String> sevenElements = createArrayList("rock", "fire", "scissors", "sponge", "paper", "air", "water");
		ArrayList<String> fifteenElements = createArrayList("rock", "fire", "scissors", "snake", "human",
				"tree", "wolf", "sponge", "paper", "air",
				"water", "dragon", "devil", "lightning", "gun");


		System.out.print("Enter your name: ");
		String name = scanner.next();
		System.out.println("Hello, " + name);
		scanner.nextLine();

		String listOfOption = scanner.nextLine();
		if (!Objects.equals(listOfOption, "")) {
			int count = 0;
			String[] options = listOfOption.toLowerCase().split(",");
			for (String option: options) {
				option = option.strip();
				if (fiveElements.contains(option)) {
					count++;
				} else if (sevenElements.contains(option)) {
					count++;
				} else if (fifteenElements.contains(option)) {
					count++;
				}
			}

			switch (count) {
				case 5 -> elements = fiveElements;
				case 7 -> elements = sevenElements;
				case 15 -> elements = fifteenElements;
			}
		}

		System.out.println("Okay, let's start");
		boolean isThere = readFile(name);

		boolean isDone = false;
		while (!isDone) {
			int number = random.nextInt(0, elements.size());
			String input = scanner.next();
			isDone = startRPS(elements.get(number), input, elements);
		}

		addToFile(name, isThere);
		scanner.close();
	}

	private static ArrayList<String> createArrayList(String... name) {
		ArrayList<String> temp = new ArrayList<>();
		for (String s : name) {
			temp.add(s);
		}

		return temp;
	}

	private static boolean readFile(String name) {
		File file = new File(FILEPATH);

		if (file.exists()) {
			int size = 1;
			try (FileReader fileReader = new FileReader(FILEPATH)) {
				while (fileReader.ready()) {
					int word = fileReader.read();
					if (word == 10) {
						size++;
					}
				}
			} catch (IOException ioe) {
				System.out.println(ioe);
			}


			try (FileReader fileReader = new FileReader(FILEPATH)) {
				words = new StringBuilder[size];
				int count = 0;
				words[count] = new StringBuilder();

				while (fileReader.ready()) {
					int word = fileReader.read();

					if (word != 10) {
						words[count].append((char) word);
					} else {
						count++;
						words[count] = new StringBuilder();
					}
				}

				boolean isThere = false;
				for (StringBuilder word: words) {
					if (word.toString().contains(name)) {
						isThere = true;
						break;
					}
					i++;
				}

				if (isThere) {
					String ratings = String.valueOf(words[i]);
					counter = Integer.parseInt(ratings.split(" ")[1]);
					return true;
				}
			} catch (IOException ioe) {
				System.out.println(ioe);
			}
		}
		return false;
	}

	private static ArrayList<String> winAgainst(String input, ArrayList<String> elements) {
		ArrayList<String> winOver = new ArrayList<>();
		final int middle = elements.size() / 2;
		int index = elements.indexOf(input);

		if (elements.size() == 3) {
			if (index == 0) {
				index = 3;
			}

			winOver.add(elements.get(index - 1));
		} else if (elements.size() == 5) {
			switch (index) {
				case 0 -> {
					winOver.add(elements.get(2));
					winOver.add(elements.get(3));
				}
				case 1 -> {
					winOver.add(elements.get(0));
					winOver.add(elements.get(4));
				}
				case 2 -> {
					winOver.add(elements.get(1));
					winOver.add(elements.get(3));
				}
				case 3 -> {
					winOver.add(elements.get(1));
					winOver.add(elements.get(4));
				}
				case 4 -> {
					winOver.add(elements.get(0));
					winOver.add(elements.get(2));
				}
			}
		} else {
			int j, count;
			for (j = index + 1, count = 0; count < middle; j++, count++) {
				if (j == elements.size()) {
					j = 0;
				}
				winOver.add(elements.get(j));
			}
		}
		return winOver;
	}

	private static boolean startRPS(String computerInput, String userInput, ArrayList<String> elements) {
		userInput = userInput.toLowerCase();

		switch (userInput) {
			case "!exit" -> {
				System.out.println("Bye");
				return true;
			}
			case "!rating" -> System.out.println("Your rating: " + counter);

			default -> {
				if (!elements.contains(userInput)) {
					System.out.println("Invalid input");
				} else {
					ArrayList<String> victoryOver = winAgainst(userInput, elements);
					if (userInput.equals(computerInput)) {
						System.out.printf("There is a draw (%s)\n", userInput);
						counter += 50;
					} else {
						if (victoryOver.contains(computerInput)) {
							System.out.printf("Well done. The computer chose %s and failed\n", computerInput);
							counter += 100;
						} else {
							System.out.println("Sorry, but the computer chose " + computerInput);
						}
					}
				}
			}
		}
		return false;
	}

	private static void addToFile(String name, boolean isThere) {
		if (!isThere) {
			try(FileWriter fileWriter = new FileWriter(FILEPATH, true)) {
				fileWriter.append(name + " " + counter);
			} catch (IOException e) {
				System.out.println(e);
			}
		} else {
			try(FileWriter fileWriter = new FileWriter(FILEPATH)) {
				words[i] = new StringBuilder(name + " " + counter);

				for (StringBuilder word: words) {
					fileWriter.append(word + "\n");
				}
			} catch (IOException ioe) {
				System.out.println(ioe);
			}
		}
	}

}
