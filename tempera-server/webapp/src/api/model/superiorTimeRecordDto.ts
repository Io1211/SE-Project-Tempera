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


export interface SuperiorTimeRecordDto { 
    Id?: number;
    stationId?: string;
    accessPointId?: string;
    start?: string;
    end?: string;
    state?: SuperiorTimeRecordDto.StateEnum;
}
export namespace SuperiorTimeRecordDto {
    export type StateEnum = 'AVAILABLE' | 'MEETING' | 'OUT_OF_OFFICE' | 'DEEPWORK';
    export const StateEnum = {
        Available: 'AVAILABLE' as StateEnum,
        Meeting: 'MEETING' as StateEnum,
        OutOfOffice: 'OUT_OF_OFFICE' as StateEnum,
        Deepwork: 'DEEPWORK' as StateEnum
    };
}

