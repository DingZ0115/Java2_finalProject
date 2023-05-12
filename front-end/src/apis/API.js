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