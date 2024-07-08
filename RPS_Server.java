/*
 * @File Name: RPS_Server.java
 * @Name: Edom Belayneh
 * @Homework #: 5
 * @Due Date: March 19, 2024 @ 11:59 pm
 * @Description: Server side of Rock Paper Scissors Game
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class RPS_Server extends Application implements RPS_Constants {
	private List<HandleASession> clients = new ArrayList<>();
	private TextArea textArea;
	private int sessionNum = 1;
	//	public static int move1;
	//	public static int move2;

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("RPS Server");

		textArea = new TextArea();
		textArea.setEditable(false);
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(textArea);

		VBox vBox = new VBox();
		vBox.getChildren().add(scrollPane);

		Scene scene = new Scene(vBox, 400, 300);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Start a new thread to handle server operations
		new Thread(() -> {
			try {
				ServerSocket serverSocket = new ServerSocket(8000);
				appendText("Server started.");
				// Continuously accept client connections and handle game sessions
				while (true) {
					appendText("Waiting for players to join session " + sessionNum);
					// Accept player 1 connection
					Socket player1 = serverSocket.accept();
					appendText("Player 1 joined session " + sessionNum);
					appendText("Player 1's IP address: " + player1.getInetAddress().getHostAddress());

					new DataOutputStream(player1.getOutputStream()).writeInt(1);
					// Accept player 2 connection
					Socket player2 = serverSocket.accept();
					appendText("Player 2 joined session " + sessionNum);
					appendText("Player 2's IP address: " + player2.getInetAddress().getHostAddress());

					new DataOutputStream(player2.getOutputStream()).writeInt(2);

					appendText("Start a thread for session " + sessionNum);
					new Thread(new HandleASession(player1, player2)).start();

					sessionNum++;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}
	/**
	 * Append text to the server log TextArea.
	 *
	 * @param text Text to append
	 */
	private synchronized void appendText(String text) {
		Platform.runLater(() -> textArea.appendText(text + "\n"));
	}

	/**
	 * Inner class to handle a game session between two players.
	 */
	private class HandleASession implements Runnable {
		private Socket player1;
		private Socket player2;

		private DataInputStream fromPlayer1;
		private DataOutputStream toPlayer1;
		private DataInputStream fromPlayer2;
		private DataOutputStream toPlayer2;

		// Continue to play
		private boolean continueToPlay = true;

		public HandleASession(Socket player1, Socket player2) {
			this.player1 = player1;
			this.player2 = player2;
		}

		/**
		 * Method to run the game session logic.
		 */
		public void run() {
			try {
				fromPlayer1 = new DataInputStream(player1.getInputStream());
				toPlayer1 = new DataOutputStream(player1.getOutputStream());
				fromPlayer2 = new DataInputStream(player2.getInputStream());
				toPlayer2 = new DataOutputStream(player2.getOutputStream());

				toPlayer1.writeInt(1); //notify player 1 that they're player one
				toPlayer2.writeInt(2); //notify player 2 that they're player two

				// Continuously serve the players and determine and report
				// the game status to the players
				while(true) {
					// Receive a move from player 1
					int move1 = fromPlayer1.readInt();
					System.out.println("Server readInt: " + move1);
					// Receive a move from Player 2
					int move2 = fromPlayer2.readInt();
					System.out.println("Server readInt: " + move2);

					if (move1 == move2) {
						toPlayer1.writeInt(DRAW);
						toPlayer2.writeInt(DRAW);
					}
					//Rock defeats (breaks) Scissors; 
					else if ((move1 == ROCK && move2 == SCISSORS) ||
							//Paper defeats (covers) Rock; 
							(move1 == PAPER && move2 == ROCK) ||
							//Scissors defeats (cuts) Paper; 
							(move1 == SCISSORS && move2 == PAPER)) {

						toPlayer1.writeInt(PLAYER1_WON);
						toPlayer2.writeInt(PLAYER1_WON);


						break;
					}
					//Rock defeats (breaks) Scissors; 
					else if(((move2 == ROCK && move1 == SCISSORS) ||
							//Paper defeats (covers) Rock; 
							(move2 == PAPER && move1 == ROCK) ||
							//Scissors defeats (cuts) Paper; 
							(move2 == SCISSORS && move1 == PAPER))){

						toPlayer1.writeInt(PLAYER2_WON);
						toPlayer2.writeInt(PLAYER2_WON);

						break;
					}

					else {
						toPlayer1.writeInt(CONTINUE);
						toPlayer2.writeInt(CONTINUE);

					}
				}
			}
			catch(IOException ex) {
				ex.printStackTrace();
			}

		}

	}
}