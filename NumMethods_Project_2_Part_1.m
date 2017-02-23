
%Project 2 Part 1 

%Input: function, method of integration, endpoints, number of iterations
%Output: numerical solution of integral

syms('a', 'b', 'c', 'd', 'e', 'm', 'n', 'i', 'j', 'h', 's', 'k', 'x', 'y')
syms('F', 'J1', 'J2', 'J3', 'K1', 'K2', 'K3', 'Q', 'P')
syms('OK', 'METHOD')
syms('r', 'const')
syms('H1', 'H2', 'C1', 'D1')

TRUE = 1;
FALSE = 0;

fprintf(1, 'Input the function f(x,y), in terms of x & y\n');
s = input(' ','s');
F = inline(s, 'x', 'y');
fprintf(1, ' \n');
fprintf(1, 'Method of Integration?\n');
fprintf(1, '1. Composite Simpson''s\n');
fprintf(1, '2. Gaussian Quadrature\n');
OK = FALSE;

%INTEGRATION METHOD DETECTOR 
while OK == FALSE
    fprintf(1, 'Enter 1 or 2\n');
    METHOD = input(' ');
    
    if METHOD >= 1 && METHOD <=2
        OK = TRUE;
    else
        fprintf(1, '1 OR 2 \n');
    end
end

%---SIMPSON'S CODE----------------------------------------------------------
if METHOD == 1
    fprintf(1, 'Using Simpson''s\n');
    OK = FALSE;
    
    %FIRST INTEGRAND BOUNDS 
    while OK == FALSE 
        fprintf(1, 'Input the bounds of integration of x [lower -> upper] on separate lines\n');
        a = input (' ');
        b = input (' ');
        
        if a == b
            fprintf(1, 'a has to be different from b! \n');
        else
            OK = TRUE;
        end
    end
    
    OK = FALSE;
    
    %SECOND INTEGRAND BOUNDS
    %FUNCTION BOUNDS: 
    while OK == FALSE
        fprintf(1, 'Input the bounds of y as y(x) or a number [lower -> upper], on separate lines\n');
        s = input(' ', 's');
        c = inline(s, 'x');
        s = input(' ', 's');
        d = inline(s, 'x');
        OK = TRUE;
    end
       
    OK = FALSE;
    while OK == FALSE
        fprintf(1, 'Input the number of steps [(m) for y -> (n) for x]\n');
        m = input (' ');
        n = input (' ');
        OK = TRUE;
    end
    OK = FALSE;
    
    i = 0;
    j = 1;
    h = (b-a)/n; %STEP SIZE 
    J1 = 0; %END TERMS
    J2 = 0; %EVENS 
    J3 = 0; %ODDS 
    
    while i <= n
        x = a + i*h;
        k = (d(x) - c(x))/m;
        K1 = (F(x,c(x)) + F(x,d(x))); %END TERMS 
        K2 = 0;                       %EVENS
        K3 = 0;                       %ODDS
        
        while j <= m-1 %nested while 
            y = c(x) + j*k;
            Q = F(x,y); 
            
            if mod(j,2) == 0 %check for even number
                K2 = K2 + Q;
            else
                K3 = K3 + Q;
            end
            j = j+1;
        end

        L = k*(K1 + 2*K2 + 4*K3)/3;
        
        if i == 0 || i == n
            J1 = J1 + L;
        elseif mod(i,2) == 0
            J2 = J2 + L;
        else
            J3 = J3 + L;
        end
        i = i+1;
        j = 1;
    end
    P = h*(J1 + 2*J2 + 4*J3)/3;
    fprintf(1, 'The value of the integral is: %9.10f\n', P);
    
end
%-----------------------------------------------------------------

% GAUSSIAN QUADRATURE CODE ---------------------------------------
if METHOD == 2
    fprintf(1, 'Using Gaussian\n');
    OK = FALSE;
    while OK == FALSE
        fprintf(1, 'Input the bounds of x [lower -> upper] on separate lines\n');
        a = input (' ');
        b = input (' ');
        if a == b
            fprintf(1, 'a must not equal b!\n');
        else
            OK = TRUE;
        end
    end
    OK = FALSE;
    while OK == FALSE
        fprintf(1, 'Input the bounds of y [lower -> upper] on separate lines\n');
        s = input(' ', 's');
        c = inline(s, 'x');
        
        s = input(' ', 's');
        d = inline(s, 'x');
        
        OK = TRUE;
    end
    
    OK = FALSE;
    
    while OK == FALSE
        fprintf(1, 'Input the number of steps (max 5) [(m) for y -> (n) for x] on separate lines \n');
        m = input (' ');
        n = input (' ');
        OK = TRUE;
    end
    
    OK = FALSE;
    
    while OK == FALSE
        r = zeros(4,5);
        const = zeros(4,5);
        %POLYNOMIALS 
        r(1,1) = 0.5773502692;
        r(1,2) = -r(1,1);
        r(2,1) = 0.7745966692;
        r(2,2) = 0.0;
        r(2,3) = -r(2,1);
        r(3,1) = 0.8611363116;
        r(3,2) = 0.3399810436;
        r(3,3) = -r(3,2);
        r(3,4) = -r(3,1);
        r(4,1) = 0.9061798459;
        r(4,2) = 0.5384693101;
        r(4,3) = 0.0;
        r(4,4) = -r(4,2);
        r(4,5) = -r(4,1);
        
        const(1,1) = 1.0;
        const(1,2) = 1.0;
        const(2,1) = 0.5555555556;
        const(2,2) = 0.8888888889;
        const(2,3) = 0.5555555556;
        const(3,1) = 0.3478548451;
        const(3,2) = 0.6521451549;
        const(3,3) = 0.6521451549;
        const(3,4) = 0.3478548451;
        const(4,1) = 0.2369268850;
        const(4,2) = 0.4786286705;
        const(4,3) = 0.5688888889;
        const(4,4) = 0.4786286705;
        const(4,5) = 0.2369286705;
        OK = TRUE;
    end
    
    H1 = (b - a)/2;
    H2 = (b + a)/2;
    
    Q = 0;
    i = 1;
    j = 1;
    
    while i <= m
        J1 = 0;
        x = H1 * r(m-1,i) + H2;
        C1 = c(x);
        D1 = d(x);
        K1 = ( D1 - C1 ) / 2;
        K2 = ( D1 + C1 ) / 2;
        
        while j <= n
            y = K1*r(n-1,j) + K2;
            P = F(x,y);
            J1 = J1 + const(n-1,j) * P;
            j = j + 1;
        end
        
        Q = Q + const(m-1,i) * K1 * J1;
        i = i + 1;
        j = 1;
    end
    
    Q = H1*Q;
    
    fprintf(1, 'The integral is equal to : %9.10f\n', Q);
end
 
