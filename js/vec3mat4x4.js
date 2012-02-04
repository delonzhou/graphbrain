/**
 * (c) 2012 GraphBrain Ltd. All rigths reserved.
 */


/**
 * Collection of linear algebra functions for vectors with 3 elements
 * and 4x4 matrices.
 * Useful for 3D calculations.
 */


// Auxiliary vector to be used in calculations
tmpVec = new Array(3);


/**
 * Caluculates the dot product of a and b,
 * where a and b are vectors with 3 elements.
 */
var v3dotv3 = function(a, b)
{
    return (a[0] * b[0]) + (a[1] * b[1]) + (a[2] * b[2]);
};


/**
 * r = m * v
 *
 * m: 4x4 matrix
 * v: vector with 3 elements
 * r: vetor with 3 elements to store results
 */
var m4x4mulv3 = function(m, v, r)
{
    var w;
    tmpVec[0] = m[3];
    tmpVec[1] = m[7];
    tmpVec[2] = m[11];
    w = v3dotv3(v, tmpVec) + m[15];
    tmpVec[0] = m[0];
    tmpVec[1] = m[4];
    tmpVec[2] = m[8];
    r[0] = (v3dotv3(v, tmpVec) + m[12]) / w;
    tmpVec[0] = m[1];
    tmpVec[1] = m[5];
    tmpVec[2] = m[9];
    r[1] = (v3dotv3(v, tmpVec) + m[13]) / w;
    tmpVec[0] = m[2];
    tmpVec[1] = m[6];
    tmpVec  [2] = m[10];
    r[2] = (v3dotv3(v, tmpVec) + m[14]) / w;
};