// Lab18avst.java
// The Student Records Algorithm Program
// This is the student, starting version of the Lab18a lab assignment.


import java.io.*;
import java.util.Arrays;
import java.util.*;
import java.text.DecimalFormat;


public class Lab18avst
{
	public static void main(String[] args) throws IOException
	{
		List studentArray = new List(100);
		studentArray.getList();
		studentArray.display("UNSORTED LIST OF STUDENTS");
		studentArray.pause();

		studentArray.gpaSort();
		studentArray.display("STUDENTS SORTED IN DESCENDING ORDER BY GPA");
		studentArray.pause();

		studentArray.ageSort();
		studentArray.display("STUDENTS SORTED IN ASCENDING ORDER BY AGE");
		studentArray.pause();

		studentArray.idSort();
		studentArray.display("STUDENTS SORTED IN ASCENDING ORDER BY ID#");
		studentArray.pause();

		int studentID = getID();
		int index = studentArray.search(studentID);

		if (index == -1)
		    System.out.println("There is no student with an ID# of "+studentID+".\n");
		else
		{
			studentArray.displayStudent(index);   // displays the information for the found student
			studentArray.delete(index); // remove the same student from the array
			studentArray.idSort(); // sort array again
			studentArray.display("STUDENTS SORTED IN ASCENDING ORDER BY ID# WITHOUT STUDENT# "+studentID);
			studentArray.pause();
		}
	}

	public static int getID()
	{
		Scanner input = new Scanner(System.in);
		System.out.print("\nEnter the 6-digit ID of the student.  { 100000 - 999999 }  -->  ");
		return input.nextInt();
	}
}


class Person
{
	public String name;
	public int id;
	public int age;
	public double gpa;

	Person(String n, int ID, int a,double g)
	{
		name = n;
		id = ID;
		age = a;
		gpa = g;
	}
	
}

class PersonGPAComparator implements Comparator<Person> {
    public int compare(Person person1, Person person2) {
    	if (person1 !=null && person2 !=null) {
        return person1.gpa < person2.gpa ? 1 : -1;
    	}
    	else return 0;
    }
}


class List
{
	private Person student[];	// stores array elements
	private int capacity;		// actual array capacity
	private int size;			// number of elements in the array

	public List(int c)
	{
		capacity = c;
		size = 0;
		student = new Person[capacity];
	}

	public void getList() throws IOException
	{
		FileReader inFile = new FileReader("src/students2.dat");
		BufferedReader inStream = new BufferedReader(inFile);
		String s1,s2,s3,s4;
		int age, id;
		double gpa;
		int index = 0;
		while( ((s1 = inStream.readLine()) != null) &&
			   ((s2 = inStream.readLine()) != null) &&
			   ((s3 = inStream.readLine()) != null) &&
			   ((s4 = inStream.readLine()) != null) )
		{
			String name = s1;
			id = Integer.parseInt(s2);
			age = Integer.parseInt(s3);
			gpa = Double.parseDouble(s4);

			student[index] = new Person(name,id,age,gpa);
			index++;
		}
		inStream.close();
		size = index;
	}

	private String spaces(String name)
    {
    	int tab = 24 - name.length();
    	String temp = "";
    	for (int j = 1; j <= tab; j++)
    	    temp += " ";
    	return temp;
    }

	public void display(String listInfo)
	{
		DecimalFormat output = new DecimalFormat("0.000");
		System.out.println("\nDISPLAYING "+ listInfo);
		System.out.println("\nStudent ID#     Student Name            Age         GPA");
		System.out.println("============================================================");

		for (int k = 0; k < size; k++)
			System.out.println(student[k].id + "          "+student[k].name + spaces(student[k].name) + student[k].age + "          " + output.format(student[k].gpa));
	}

	public void pause()
	{
		Scanner input = new Scanner(System.in);
		String dummy;
		System.out.println("\nPress <Enter> to continue.");
		dummy = input.nextLine();
	}

	public void displayStudent(int index)
	{
		DecimalFormat output = new DecimalFormat("0.000");
		System.out.println(student[index].id + "          "+student[index].name + spaces(student[index].name) + student[index].age + "          " + output.format(student[index].gpa));
	}

	private void swap(int x, int y)
	{
		Person temp = student[y];
		student[y] = student[x];
		student[x] = temp;

	}

	public void gpaSort()
	{
		boolean sorted;
	       int p = 1;
	       do
	       {
			sorted = true;
			for (int q = 0; q < size-p; q++)
		       	if (student[q].gpa < student[q+1].gpa)
		       	{			
					swap(q,q+1);
					sorted = false;
		       	}
			p++;
	       }
	       while (!sorted);

		

	}
	/*public void ageSort()
	{
	       int p,q;
	       int smallest;
	       for (p = 0; p < size-1; p++)
	       {
			smallest = p;
			for (q = p+1; q < size-1; q++)
				if (student[q].age < student[smallest].age)
		       		smallest = q;
			if (student[p] != student[smallest])
		       	swap(p,smallest); 
	       }
	} */
	private void merge(int first, int mid, int last)
   	{		
		Person tempArray[] = new Person[size];
      		int p = first;  int q = mid+1;  int k = first;
     		while (p <= mid && q <= last)
      	{
         			if (student[p].age <= student[q].age)
         			{
            			tempArray[k] = student[p];
            			p++;
         			}
         			else
         			{
            			tempArray[k] = student[q];
            			q++;
         			}
        			k++;
      		}
      		while (p <= mid)
      		{
         			tempArray[k] = student[p];
         			p++;   k++;
      		}
      		while (q <= last)
      		{
         			tempArray[k] = student[q];
         			q++;   k++;
      		}
      		for (int h = first; h <= last; h++)
         			student[h] = tempArray[h];
   	}
	public void mergeSort(int first, int last)
	{
		if (first < last)
		{
			int mid = (first + last) / 2;
			mergeSort(first,mid);
			mergeSort(mid+1,last);
			merge(first,mid,last);
		}
	}
	public void ageSort()
	{
		this.mergeSort(size-size, size-1);
//		boolean sorted;
//	       int p = 1;
//	       do
//	       {
//			sorted = true;
//			for (int q = 0; q < size-p; q++)
//		       	if (student[q].age > student[q+1].age)
//		       	{	
//		       		//System.out.println(student[q].age + " is Greater than " + student[q+1].age);
//					swap(q,q+1);
//					sorted = false;
//		       	}
//		       	else {
//				//System.out.println(student[q].age + " is Less than " + student[q+1].age + " so we will skip it");
//			p++;
//		       	}
//			
//	       }
//	       while (!sorted);

	}

	public void idSort()
	{
		boolean sorted;
	       int p = 1;
	       do
	       {
			sorted = true;
			for (int q = 0; q < size-p; q++)
		       	if (student[q].id > student[q+1].id)
		       	{			
					swap(q,q+1);
					sorted = false;
		       	}
			p++;
	       }
	       while (!sorted);
	}

	public int search(int studentID)
	{
		this.idSort();
		boolean found = false;
		int lo = 0;
		int hi = size-1;
		int mid = 0;
		while (lo <= hi && !found)
		{
			mid = (lo + hi) / 2;
			if (student[mid].id == studentID)
				found = true;
			else
			{
				if (studentID > student[mid].id)
					lo = mid + 1;
				else
					hi = mid - 1;
			}
		}
		if (found) {
			this.idSort();
			return mid;
		}
		else
			return -1;



	}

	public void delete(int index)
	{
		size--;
		swap(index, size);
		// size--;
		Person temp[] = new Person[size];
		for (int q = 0; q < size; q++) {
			temp[q] = student[q];
		}
		this.idSort();
		student = temp;
	}
}

