package com.ioappstudio.emicalculator.viewmodels

import androidx.lifecycle.ViewModel
import com.ioappstudio.emicalculator.data.CalculateLoanInterest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.pow

class CalculateLoanInterestViewModel : ViewModel() {

    private var _interestViewModel =
        MutableStateFlow(CalculateLoanInterest(principal = 0f, interest = 0f, tenure = 0f))
    val interestViewModel = _interestViewModel


    var principal = ""
    var interest = ""
    var tenure = ""


    fun calculateTheLoan(
        principal: Float,
        interest: Float,
        tenure: Float
        /*finalResult: (Float, Float) -> Unit*/
    ) {

        this.principal = principal.toString()
        this.interest = interest.toString()
        this.tenure = tenure.toString()

        //EMI Formula = [P x R x (1+R)^N]/[(1+R)^N-1]
        val interestInternal = interest / (12 * 100) // one month interest
        val tenureInternal = tenure * 12 // one month period
        val emi =
            ((principal * interestInternal * (1 + interestInternal).pow(tenureInternal)) / ((1 + interestInternal).pow(
                tenureInternal
            ) - 1))
        val totalInterest = emi * tenureInternal
        val interestOnly = totalInterest - principal

        this._interestViewModel.value = CalculateLoanInterest(
            principal = principal, interest = interestOnly,
            tenure = totalInterest, emi = emi
        )

    }
}