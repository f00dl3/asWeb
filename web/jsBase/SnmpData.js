/* 
by Anthony Stump
Created: 30 Mar 2018
 */

function checkIfSnmpIsUp(state) {
    switch(state) {
        case true: doSnmpWidget(); break;
        case false: break;
    }
}