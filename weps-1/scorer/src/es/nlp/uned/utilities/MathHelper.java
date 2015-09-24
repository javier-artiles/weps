package es.nlp.uned.utilities;

/**
 * The Class MathHelper.
 */
public class MathHelper {
	
	/**
	 * Round.
	 * 
	 * @param decimals the decimals
	 * @param number the number
	 * 
	 * @return the double
	 */
	public static double round( double number, int decimals ) {
	    return Math.round(number*Math.pow(10,decimals))/Math.pow(10,decimals);
	}
	
	
	/**
	 * Al normalizar convertimos la distancia euclidea en una medida de similitud.
	 * 
	 * @param vector_1 the vector_1
	 * @param vector_2 the vector_2
	 * @param normalized the normalized
	 * 
	 * @return the double
	 */
	public static double euclideanDistance(int[] vector_1, int[] vector_2, boolean normalized){
		assert(vector_1.length == vector_2.length);
		
		double result = 0.0;
		
		for(int i=0; i<vector_1.length; i++){
			//System.out.println(vector_1[i]+" : "+vector_2[i]);
			
			result += Math.pow(vector_1[i] - vector_2[i], 2);
			
		}
	
		result = Math.sqrt(result);
		//System.out.println(result);
		if(normalized){
			result = 1/(double)(1+result);
		}
		
		return result;
	}
	
	
	/**
	 * Prints the matrix.
	 * 
	 * @param matrix the matrix
	 * @param rows the rows
	 * @param columns the columns
	 */
	public static void printMatrix(String[] columns, String[] rows, Object[][] matrix){
		
		System.out.print("\t");
		
		for(int col=0; col<matrix.length; col++){
			System.out.print(columns[col].toString());
			if(col+1<matrix.length) System.out.print("\t");
		}
		
		System.out.print("\n");
		
		for(int row=0; row<matrix.length; row++){
			
			System.out.print(rows[row]);
			System.out.print("\t");
			
			for(int col=0; col<matrix.length; col++){
				
				System.out.print(matrix[row][col]);
				
				if(col+1<matrix.length) System.out.print("\t");
				
			}
			
			System.out.print("\n");
			
		}
		
	}
	
}
