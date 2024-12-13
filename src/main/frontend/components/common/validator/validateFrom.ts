import React from "react";

export const fnFormValidataion = (formRef: React.RefObject<HTMLFormElement | null> ): boolean => {

    if (!formRef.current) {
        throw new Error("Form reference is null.");
    }

    let isValid = true;

    //Form에 속한 모든 input 요소 체크
    const inputs = formRef.current.querySelectorAll("input");

    inputs.forEach((input) => {

        const value = input.value;

        //정수만 허용
        if (input.classList.contains("onlyNumber")) {
            if(!fnValidNumber(value)) {
                alert(`${input.title} : 정수만 입력 가능합니다.`);
                input.focus();
                isValid = false;
                return;
            }
        }

        //부호 허용 정수만 허용
        if (input.classList.contains("onlySignNumber")) {
            if(!fnValidSignNumber(value)) {
                alert(`${input.title} : 부호를 포함한 정수만 입력 가능합니다.`);
                input.focus();
                isValid = false;
                return;
            }
        }

        //실수형만 허용
        if (input.classList.contains("onlyFloat")) {
            if(!fnValidFloat(value)) {
                alert(`${input.title} : 실수만 입력 가능합니다.`);
                input.focus();
                isValid = false;
                return;
            }
        }

        //부호 허용 실수형만 허용
        if (input.classList.contains("onlySignFloat")) {
            if(!fnValidSignFloat(value)) {
                alert(`${input.title} : 부호를 포함한 실수만 입력 가능합니다.`);
                input.focus();
                isValid = false;
                return;
            }
        }

        //숫자와 영문만 포함 허용
        if (input.classList.contains("onlyAlpaNumber")) {
            if(!fnValidAlpaNumber(value)) {
                alert(`${input.title} : 정수만 입력 가능합니다.`);
                input.focus();
                isValid = false;
                return;
            }
        }

        //날짜 점검 처리
        if (input.classList.contains("onlyDate")) {

            //class에 none-hyphen인 경우에는 하이픈을 제거하고 점검한다.
            let isHyphen = true;
            if( input.classList.contains("none-hyphen") ) isHyphen = false;

            if(!fnValidDate(value, isHyphen)) {
                alert(`${input.title} : 정수만 입력 가능합니다.`);
                input.focus();
                isValid = false;
                return;
            }
        }

        //전화번호 점검 처리
        if (input.classList.contains("onlyPhone")) {

            //class에 none-hyphen인 경우에는 하이픈을 제거하고 점검한다.
            let isHyphen = true;
            if( input.classList.contains("none-hyphen") ) isHyphen = false;

            if(!fnValidPhoneNumber(value, isHyphen)) {
                alert(`${input.title} : 정수만 입력 가능합니다.`);
                input.focus();
                isValid = false;
                return;
            }
        }

        //전화번호 점검 처리
        if (input.classList.contains("onlyEmail")) {

            if(!fnValidEmail(value)) {
                alert(`${input.title} : 정수만 입력 가능합니다.`);
                input.focus();
                isValid = false;
                return;
            }
        }

    });
    
    return isValid;
};

/**
 * 정수인지 판단
 * @param value 
 * @returns 
 */
const fnValidNumber = (value: string): boolean => {
    
    if( value.trim() === '' ) return true;
    value = value.trim();

    const regex = /^[1-9]+\d*$/; // 정수만 허용
    return regex.test(value);
}

/**
 * 부호를 포함한 정수인지 판단.
 * @param value 
 * @returns 
 */
const fnValidSignNumber = (value: string): boolean => {
    
    if( value.trim() === '' ) return true;
    value = value.trim();

    // 정규식 : 0 또는 부호가 붙은 정수
    const regex = /^[+-]?(\d*[1-9]\d*|0)$/; // 정수만 허용
    return regex.test(value);
}

/**
 * 실수형인지 확인
 * @param value 
 * @returns 
 */
const fnValidFloat = (value: string): boolean => {
    
    if( value.trim() === '' ) return true;
    value = value.trim();

    const regex = /^(0|[1-9]\d*)(\.\d+)?$/;
    return regex.test(value);
}

/**
 * 부호를 허용한 실수형인지 확인
 * @param value 
 * @returns 
 */
const fnValidSignFloat = (value: string): boolean => {
    
    if( value.trim() === '' ) return true;
    value = value.trim();

    const regex = /^[+-]?(0|[1-9]\d*)(\.\d+)?$/;
    return regex.test(value);
}


/**
 * 숫자와 영문인지 확인
 * @param value 
 * @returns 
 */
const fnValidAlpaNumber = (value: string): boolean => {
    
    if( value.trim() === '' ) return true;
    value = value.trim();

    const regex = /^[0-9a-zA-Z]*$/;
    return regex.test(value);
}

/**
 * 날짜형(YYYY-MM-DD) 확인
 * @param value 
 * @returns 
 */
const fnValidDate = (value: string, hyphen: boolean): boolean => {
    
    if( value.trim() === '' ) return true;
    value = value.trim();

    let regex = null;
    if( hyphen ) {
        regex = /^(19|20)\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/;
    } else {
        regex = /^(19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])$/;
    }
    return regex.test(value);
}

/**
 * 휴대전화번호 형식 확인
 * @param value 
 * @returns 
 */
const fnValidPhoneNumber  = (value: string, hyphen: boolean): boolean => {
    
    if( value.trim() === '' ) return true;
    value = value.trim();

    let regex = null;
    if( hyphen ) {
        regex = /^(19|20)\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])$/;
    } else {
        regex = /^(19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])$/;
    }

    return regex.test(value);
}

/**
 * 이메일 확인
 * @param value 
 * @returns 
 */
const fnValidEmail = (value: string): boolean => {
    
    if( value.trim() === '' ) return true;
    value = value.trim();

    const regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return regex.test(value);
}
