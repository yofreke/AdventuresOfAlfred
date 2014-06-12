package com.yofreke.alfred;

public class Perlin {

	public float octi = 8f;
	public float p = 1.5f;
	
	public Perlin(){
	}
	
	public static float IntNoise(int x){			 
		x = (x<<13) ^ x;
	    return ( 1.0f - ( (x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0f);    
	}
	
	public static float Noise(int x, int y){
		int n = x + y * 57;
		n = (n<<13) ^ n;
		return ( 1.0f - (int)( (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0f);    
	}

	public float SmoothNoise1(int x, int y){
		float corners = ( Noise(x-1, y-1)+Noise(x+1, y-1)+Noise(x-1, y+1)+Noise(x+1, y+1) ) / 16;
		float sides = ( Noise(x-1, y) + Noise(x+1, y) + Noise(x, y-1) + Noise(x, y+1) ) /  8;
		float center = Noise(x, y) / 4;
		return corners + sides + center;
	}

	public float Linear_Interpolate(float a, float b, float x){
		return  a*(1-x) + b*x;
	}
	public float Cosine_Interpolate(float a, float b, float x){
		float ft = x * 3.1415927f;
		float f = (float) ((1 - Math.cos(ft)) * 0.5f);

		return  a*(1-f) + b*f;
	}

	public float InterpolatedNoise_1(float x, float y){

		int integer_X = (int)(x);
		float fractional_X = x - integer_X;
		if(x < 0){
			fractional_X = 1 - fractional_X;
		}

		int integer_Y = (int)(y);
		float fractional_Y = y - integer_Y;
		if(y < 0){
			fractional_Y = 1 - fractional_Y;
		}

		float v1 = SmoothNoise1(integer_X, integer_Y);
		float v2 = SmoothNoise1(integer_X + 1, integer_Y);
		float v3 = SmoothNoise1(integer_X, integer_Y + 1);
		float v4 = SmoothNoise1(integer_X + 1, integer_Y + 1);

		float i1 = Cosine_Interpolate(v1 , v2 , fractional_X);
		float i2 = Cosine_Interpolate(v3 , v4 , fractional_X);

		return Cosine_Interpolate(i1 , i2 , fractional_Y);

	}

	public float PerlinNoise_2D(float x, float y){

		float total = 0;
		float n = octi - 1;

		float frequency, amplitude;
		for(int i = 0; i < n; i++){
			frequency = (float) Math.pow(2, i);
			amplitude = (float) Math.pow(p, i);

			total = total + InterpolatedNoise_1(x * frequency, y * frequency) * amplitude;
		}

		return total;
	}
}
