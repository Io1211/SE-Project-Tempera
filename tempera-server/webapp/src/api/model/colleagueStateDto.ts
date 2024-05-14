/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


export interface ColleagueStateDto { 
    name?: string;
    workplace?: string;
    state?: ColleagueStateDto.StateEnum;
    isVisible?: boolean;
    groupOverlap?: Array<string>;
}
export namespace ColleagueStateDto {
    export type StateEnum = 'AVAILABLE' | 'MEETING' | 'OUT_OF_OFFICE' | 'DEEPWORK';
    export const StateEnum = {
        Available: 'AVAILABLE' as StateEnum,
        Meeting: 'MEETING' as StateEnum,
        OutOfOffice: 'OUT_OF_OFFICE' as StateEnum,
        Deepwork: 'DEEPWORK' as StateEnum
    };
}


