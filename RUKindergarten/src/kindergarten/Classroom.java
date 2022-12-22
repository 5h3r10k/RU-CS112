package kindergarten;
/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(filename);
        int numStudents = StdIn.readInt();
        String fn,ln;
        int height;
        Student temp;
        Student[] roster = new Student[numStudents];
        for (int i = 0; i < numStudents; i++) {
            fn=StdIn.readString();
            ln=StdIn.readString();
            height=StdIn.readInt();

            roster[i]=new Student(fn, ln, height);
        }

        for(int s=0; s<numStudents;s++){
            for (int i = 0; i < roster.length - 1; i++) {
                if(roster[i].compareNameTo(roster[i+1])>0){
                    temp=roster[i];
                    roster[i]=roster[i+1];
                    roster[i+1]=temp;
                }
            }
        }

        studentsInLine = new SNode();
        SNode students = studentsInLine;

        for(int s =0; s<roster.length; s++){
            students.setStudent(roster[s]);
            if(s<roster.length-1) students.setNext(new SNode());
            students = students.getNext();
        }

    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *  
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {
        // WRITE YOUR CODE HERE
        StdIn.setFile(seatingChart);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        seatingAvailability = new boolean[r][c];

        for (int i = 0; i < seatingAvailability.length; i++) {
            for (int j = 0; j < seatingAvailability[i].length; j++) {
                seatingAvailability[i][j]=StdIn.readBoolean();
            }
        }

        studentsSitting = new Student[r][c];
    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents () {


	    // WRITE YOUR CODE HERE
        for (int i = 0; i < seatingAvailability.length; i++) {
            for (int j = 0; j < seatingAvailability[i].length; j++) {
                if(seatingAvailability[i][j]==true && studentsInLine!=null){
                    studentsSitting[i][j] = studentsInLine.getStudent();
                    seatingAvailability[i][j]=false;
                    studentsInLine = studentsInLine.getNext();
                }
            }
        }

        studentsInLine = null; //studentsInLine is now empty
	
    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {
        
        // WRITE YOUR CODE HERE
        SNode head = new SNode();
        musicalChairs = new SNode();

        boolean studentFound = false;
        //find the first Student and assign them to head node of CLL
        for (int i = 0; i < studentsSitting.length; i++) {
            for (int j = 0; j < studentsSitting[i].length; j++) {
                if(studentsSitting[i][j]!=null){
                    head.setStudent(studentsSitting[i][j]);
                    studentsSitting[i][j]=null;
                    seatingAvailability[i][j]=true;
                    studentFound = true;
                    break;
                }
            }
            if(studentFound) break;
        }

        // now the head node contains the first student
        musicalChairs = head;

        for (int i = 0; i < studentsSitting.length; i++) {
            for (int j = 0; j < studentsSitting[i].length; j++) {
                if(studentsSitting[i][j]!=null){
                    musicalChairs.setNext(new SNode());
                    musicalChairs = musicalChairs.getNext();
                    musicalChairs.setStudent(studentsSitting[i][j]);
                    studentsSitting[i][j]=null;
                    seatingAvailability[i][j]=true;
                }
            }
        }

        musicalChairs.setNext(head);

    }

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the 
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in studentsInLine 
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    public void playMusicalChairs() {

        // WRITE YOUR CODE HERE
        SNode temp; //temp node
        Student[] tempLine = new Student[cllLength(musicalChairs)-1]; //student array to store students before sorting
        int n = 0; 

        while(cllLength(musicalChairs)>1){
            temp = mcPopNth(StdRandom.uniform(cllLength(musicalChairs)));
            tempLine[n] = temp.getStudent();
            n++;
        }

        Student winner = musicalChairs.getStudent(); //extracting the winner
        musicalChairs = null; //clearing the musicalChairs CLL



        // -- Sorting students into studentsInLine -- //

        //putting first student from tempLine into studentsInLine
        studentsInLine = new SNode(tempLine[0],null);

        Student tempStudent;

        //iterating through remaining students in tempLine (we start at index 1)
        for (int i = 1; i < tempLine.length; i++) {
            tempStudent = tempLine[i];

            //case 1: tempStudent's height is less than the height of the first student in studentsInLine
            if(tempStudent.getHeight()<studentsInLine.getStudent().getHeight()){
                studentsInLine = new SNode(tempStudent,studentsInLine);
            }

            //case 2: tempStudent's height is greater than the height of the first student
            else{
                SNode tempNode = studentsInLine;
                while(tempNode.getNext()!=null && tempStudent.getHeight()>tempNode.getNext().getStudent().getHeight()){
                    tempNode = tempNode.getNext();
                }
                tempNode.setNext(new SNode(tempStudent,tempNode.getNext()));
            }

        }


        // -- Seating students -- //

        //seating the winner in the first available seat
        for (int i = 0; i < seatingAvailability.length; i++) {
            for (int j = 0; j < seatingAvailability[i].length; j++) {
                if(seatingAvailability[i][j]==true){
                    seatingAvailability[i][j]=false;
                    studentsSitting[i][j]=winner;
                    i = j = seatingAvailability.length+1; // break out of both loops
                    break;
                }
            }
        }

        //seating the remaining students
        seatStudents();


    } 

    /**
     * SHOULD NOT MODIFY THE CLL POINTER
     * @param end the reference to the Circular Linked List's ending node (points to the start aka head)
     * @return the length of the circular list (the number of nodes)
     */
    private int cllLength(SNode ptr){
        SNode temp = ptr;
        int count = 1;
        while(temp.getNext()!=ptr){
            temp = temp.getNext();
            count++;
        }
        return count;
    }

    /**
     * 
     * @param end the reference to the CLL (Circular Linked List)
     * @param pos the position (index 0 being the head of the CLL) of the SNode to be removed
     * @return the SNode that's been removed
     */
    private SNode mcPopNth(int pos){
        // deal with invalid pos values
        if(pos>=cllLength(musicalChairs)||pos<0) throw new IndexOutOfBoundsException("Index out of bounds for CLL");

        // deal with cll of length 1
        if(cllLength(musicalChairs)==1) return musicalChairs;

        // get to the node before the one we want to delete
        SNode before = musicalChairs;
        for (int i = 0; i < pos; i++) {
            before = before.getNext(); //go to the next one
        }


        if(before.getNext()==musicalChairs){ 
            // System.out.println("This is the last node");
            musicalChairs = before;
        }    

        SNode popped = before.getNext();
        before.setNext(popped.getNext());
        popped.setNext(null);

        // System.out.println("POPPED "+popped.getStudent().print());
        return popped;

    }

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    public void addLateStudent ( String firstName, String lastName, int height ) {
        
        // WRITE YOUR CODE HERE

        // Creating the late student object
        Student lateStudent = new Student(firstName,lastName,height);

        // -- Inserting the student into the appropriate list -- //

        //case 1: studentsInLine is not empty, so we insert the student into studentsInLine as last node
        if(studentsInLine!=null){
            SNode temp = studentsInLine;
            while(temp.getNext()!=null){
                temp = temp.getNext();
            }
            temp.setNext(new SNode(lateStudent,null));
        }

        //case 2: musicalChairs is not empty, so we insert the student into musicalChairs as last node
        else if(musicalChairs!=null){
            // SNode temp = musicalChairs;
            // while(temp.getNext()!=musicalChairs){
            //     temp = temp.getNext();
            // }
            // System.out.println("Last student: "+temp.getStudent().print());
            // temp.setNext(new SNode(lateStudent,musicalChairs));

            musicalChairs.setNext(new SNode(lateStudent,musicalChairs.getNext()));
            musicalChairs = musicalChairs.getNext();
        }

        //case 3: studentsSitting is not empty, so we insert the student into the next available seat
        else{
            for (int i = 0; i < seatingAvailability.length; i++) {
                for (int j = 0; j < seatingAvailability[i].length; j++) {
                    if(seatingAvailability[i][j]==true){
                        seatingAvailability[i][j]=false;
                        studentsSitting[i][j]=lateStudent;
                        i = j = seatingAvailability.length+seatingAvailability[i].length+1000; // break out of both loops
                        break;
                    }
                }
            }
        }

        
    }

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students 
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName the student's last name
     */
    public void deleteLeavingStudent ( String firstName, String lastName ) {

        // WRITE YOUR CODE HERE

        // case 1: studentsInLine is not empty, so we delete the student from studentsInLine
        if(studentsInLine!=null){
            // case 1.1: the student to be deleted is the first student in studentsInLine
            if(studentsInLine.getStudent().getFirstName().toLowerCase().equals(firstName.toLowerCase()) 
            && studentsInLine.getStudent().getLastName().toLowerCase().equals(lastName.toLowerCase())){
                studentsInLine = studentsInLine.getNext();
            }

            // case 1.2: the student to be deleted is not the first student in studentsInLine
            else{
                SNode temp = studentsInLine;
                while(temp.getNext()!=null && 
                !(temp.getNext().getStudent().getFirstName().toLowerCase().equals(firstName.toLowerCase()) && 
                temp.getNext().getStudent().getLastName().toLowerCase().equals(lastName.toLowerCase()))){
                    temp = temp.getNext();
                }
                if(temp.getNext()!=null) temp.setNext(temp.getNext().getNext());
            }
        }


        // case 2: musicalChairs is not empty, so we delete the student from musicalChairs
        else if(musicalChairs!=null){
            // case 2.1: the student to be deleted is the musicalChairs node (the last node in the CLL)
            if(musicalChairs.getStudent().getFirstName().toLowerCase().equals(firstName.toLowerCase()) &&
            musicalChairs.getStudent().getLastName().toLowerCase().equals(lastName.toLowerCase())){
                SNode temp = musicalChairs;
                while(temp.getNext()!=musicalChairs){
                    temp = temp.getNext();
                }
                temp.setNext(musicalChairs.getNext());
                musicalChairs = temp;
            }

            // case 2.2: the student to be deleted is not the first student in musicalChairs
            else{
                SNode temp = musicalChairs;
                while(temp.getNext()!=musicalChairs && 
                !(temp.getNext().getStudent().getFirstName().toLowerCase().equals(firstName.toLowerCase()) && 
                temp.getNext().getStudent().getLastName().toLowerCase().equals(lastName.toLowerCase()))){
                    temp = temp.getNext();
                }
                if(temp.getNext()!=musicalChairs) temp.setNext(temp.getNext().getNext());
            }
        }

        // case 3: studentsSitting is not empty, so we delete the student from the next available seat
        else{
            for (int i = 0; i < seatingAvailability.length; i++) {
                for (int j = 0; j < seatingAvailability[i].length; j++) {
                    if(seatingAvailability[i][j]==false && studentsSitting[i][j]!=null &&
                    studentsSitting[i][j].getFirstName().toLowerCase().equals(firstName.toLowerCase()) && 
                    studentsSitting[i][j].getLastName().toLowerCase().equals(lastName.toLowerCase())){
                        seatingAvailability[i][j]=true;
                        studentsSitting[i][j]=null;
                        i = j = seatingAvailability.length+seatingAvailability[i].length+1000; // break out of both loops
                        break;
                    }
                }
            }
        }

    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

}
