import java.util.*;
import java.io.*;
import java.io.IOException;
import java.util.StringTokenizer;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;


public class QuizMenu
{
   ArrayList<String> menu = new ArrayList<String>();
   ArrayList<QuizQuestion> questionArray = new ArrayList<QuizQuestion>();
   ArrayList<QuizAnswer> answerArray = new ArrayList<QuizAnswer>();
   private int correctAnswers = 0;
   private int incorrectAnswers = 0;


   public void  createMenu()
   {
      menu.add("========");
      menu.add("Options:");
      menu.add("========");
      menu.add("R: Read question details from a text file");
      menu.add("A: Administer the quiz test");
      menu.add("F: Administer the quiz test with option to quit");
      menu.add("D: Display the answers on the scr");
      menu.add("W: Write the answers to a text file");
      menu.add("Q: Quit");
      menu.add("Please select an option:");
   }

   public void runMenu() throws Exception
   {
      createMenu();
      char choice = 'z';
      Scanner kb = new Scanner(System.in);
      while(choice != 'Q')
      {
         displayMenu();
         choice = kb.next().toUpperCase().charAt(0);
         // kb.nextLine();
         process(choice);
      }
   }
   public void displayMenu()
   {
      for(String s: menu)
      {
         System.out.println(s);
      }

   }
   public void process(char choice) throws Exception
   {
      switch (choice)
      {
         case 'R':
            readQuestions(); // I am assuming reading question details to verify the correct answers so that the quiz doesnt have any error.
            break;

         case 'A':
            administerQuiz(questionArray);
            break;

         case 'F':
            administerOptionQuit(questionArray);
            break;

         case 'D':
            displayAnswers(answerArray, questionArray);
            break;

         case 'W':
            writetoAFile(answerArray, questionArray);
            break;
      }
   }

   public void readQuestions() throws Exception
   {
      Scanner kb = new Scanner(System.in);
      System.out.println("Enter the name of file:");
      String fileName = kb.next();
      Scanner in = new Scanner(new File(fileName));
      while(in.hasNext())
      {
         String line = in.nextLine().trim();

         if(line.equalsIgnoreCase("T"))
         {
            questionArray.add(TrueFalseQuestion.readQuestion(in)); // question object is added to the question array
         }

         else if(line.equalsIgnoreCase("W"))
         {
            questionArray.add(WordQuestion.readQuestion(in));
         }
         else
         {
            questionArray.add(MultipleChoiceQuestion.readQuestion(in));
         }
      }
      // if asked only to read not to display I should delete this for loop.
      System.out.println("**************VERIFYING THE CORRECTNESS OF TEST*****************");
      int countTwo = 1;
      for(QuizQuestion q: questionArray)
      {
         System.out.println("QUESTION " + countTwo++);
         System.out.println(q);
      }
   }

   public void administerQuiz(ArrayList<QuizQuestion> questionArray)// not going through its own arrayList unless given as input argument. Why does that happen??
   {
      System.out.println("Task 2: option A:************** ADMINISTER TEST******************* : ");

      int count = 1;
      for(QuizQuestion q: questionArray)
      {
         System.out.println("\nQUESTION " + count++);
         QuizAnswer  administeredAnswer = q.administer();
         answerArray.add(administeredAnswer);
      }
   }

   public void administerOptionQuit(ArrayList<QuizQuestion> questionArray)
   {
      System.out.println("Task 2: option F: *******Administer Test with option to QUIT*****");
      Scanner kb = new Scanner(System.in);
      int count = 1;
      for(QuizQuestion q: questionArray)
      {
         System.out.println("\nQUESTION " + count++);
         QuizAnswer  administeredAnswer = q.administer();
         answerArray.add(administeredAnswer);

         System.out.print("Do you want to continue? (y/n):");
         if(kb.next().charAt(0) == 'n')
         {
         }
      }
   }

   public void displayAnswers(ArrayList<QuizAnswer> answerArray, ArrayList<QuizQuestion> questionArray)
   {
      System.out.println("Task 2: Option D: ***DISPLAYING ANSWERS ON THE SCREEN******");
      int countThree = 1;
      for(QuizAnswer ans: answerArray )
      {
         System.out.println("\nQUESTION " + countThree++);
         System.out.println(ans);
         break;
         if(ans.getResult() == 'C')
         {
            correctAnswers++;
         }
      }
      incorrectAnswers = answerArray.size()- correctAnswers;
      String summary = "Number of questions in the quiz test: " + questionArray.size() + "\n"
         + "Number of question you have answered: " + answerArray.size() + "\n"
         + "Number of correct answers: " + correctAnswers + "\n"
         + "Number of incorrect answers: " + incorrectAnswers;
      System.out.println(summary);
   }

   public void writetoAFile(ArrayList<QuizAnswer> answerArray, ArrayList<QuizQuestion> questionArray) throws IOException
   {
      System.out.println("Task 2: Option W: *******WRITING RESULT INTO A FILE******");
      Scanner kb = new Scanner(System.in);
      System.out.print("Enter the file name you want to write result into: ");
      PrintWriter outfile = new PrintWriter(new FileWriter(kb.nextLine().trim(), true));
      int countFour = 1;

      for(QuizAnswer ans: answerArray )
      {
         outfile.println("QUESTION " + countFour++ +"\n" + ans + "\n");
      }

      correctAnswers = 0; // to be initialized to 0 or else would have increased at the time of display
      for(QuizAnswer ans: answerArray )//same process is done twice.Summary of correct and incorrect results should be written to file for administering test                                      // when not displaying too.I would have written a different method also.
      {
         if(ans.getResult() == 'C')
         {
            correctAnswers++;
         }
      }
      incorrectAnswers = answerArray.size()- correctAnswers;
      String summary = "Number of questions in the quiz test: " + questionArray.size() + "\n"
         + "Number of question you have answered: " + answerArray.size() + "\n"
         + "Number of correct answers: " + correctAnswers + "\n"
         + "Number of incorrect answers: " + incorrectAnswers;

      outfile.println(summary);
      outfile.close();
   }
}
