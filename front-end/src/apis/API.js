import request from '../utils/request';

export function getNoAnswerRatio() {
    return request({
        method: 'GET',
        url: '/question/getNoAnswerRatio'
    })
}

export function getAverageAnswer() {
    return request({
        method: 'GET',
        url: '/question/getAverageAnswer'
    })
}

export function getMaxAnswer() {
    return request({
        method: 'GET',
        url: '/question/getMaxAnswer'
    })
}


export function getAcceptedQuestionCount() {
    return request({
        method: 'GET',
        url: '/question/getAcceptedQuestionCount'
    })
}

//---------------------------------
export function getDistributionOfAnswers() {
    return request({
        method: 'GET',
        url: '/question/getDistributionOfAnswers'
    })
}

export function getBetterRatio() {
    return request({
        method: 'GET',
        url: '/question/getBetterRatio'
    })
}

export function getTop5UpvoteTags() {
    return request({
        method: 'GET',
        url: '/tag/getTop5UpvoteTags'
    })
}

export function getTop5ViewTags() {
    return request({
        method: 'GET',
        url: '/tag/getTop5ViewTags'
    })
}

export function getTop15AppearWithJavaTags() {
    return request({
        method: 'GET',
        url: '/tag/getTop15AppearWithJavaTags'
    })
}

export function getUserDistributionOfPost() {
    return request({
        method: 'GET',
        url: '/user/getUserDistributionOfPost'
    })
}

export function getUserDistributionOfAnswer() {
    return request({
        method: 'GET',
        url: '/user/getUserDistributionOfAnswer'
    })
}

export function getUserDistributionOfComment() {
    return request({
        method: 'GET',
        url: '/user/getUserDistributionOfComment'
    })
}

export function getUserDistributionOfCommunication() {
    return request({
        method: 'GET',
        url: '/user/getUserDistributionOfCommunication'
    })
}

export function getMostActiveUser() {
    return request({
        method: 'GET',
        url: '/user/getMostActiveUser'
    })
}

export function getDistrutionOfQuestionDeltaTimes() {
    return request({
        method: 'GET',
        url: '/question/getDistrutionOfQuestionDeltaTimes'
    })
}

export function getJavaAPI() {
    return request({
        method: 'GET',
        url: '/tag/getJavaAPI'
    })
}
