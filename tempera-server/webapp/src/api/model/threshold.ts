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
import { ThresholdTip } from './thresholdTip';
import { Modification } from './modification';


export interface Threshold { 
    id?: number;
    defaultThreshold?: boolean;
    sensorType?: Threshold.SensorTypeEnum;
    thresholdType?: Threshold.ThresholdTypeEnum;
    value?: number;
    modification?: Modification;
    tip?: ThresholdTip;
    ofLowerBoundType?: boolean;
    modificationReason?: Modification;
}
export namespace Threshold {
    export type SensorTypeEnum = 'TEMPERATURE' | 'IRRADIANCE' | 'HUMIDITY' | 'NMVOC';
    export const SensorTypeEnum = {
        Temperature: 'TEMPERATURE' as SensorTypeEnum,
        Irradiance: 'IRRADIANCE' as SensorTypeEnum,
        Humidity: 'HUMIDITY' as SensorTypeEnum,
        Nmvoc: 'NMVOC' as SensorTypeEnum
    };
    export type ThresholdTypeEnum = 'UPPERBOUND_INFO' | 'LOWERBOUND_INFO' | 'UPPERBOUND_WARNING' | 'LOWERBOUND_WARNING';
    export const ThresholdTypeEnum = {
        UpperboundInfo: 'UPPERBOUND_INFO' as ThresholdTypeEnum,
        LowerboundInfo: 'LOWERBOUND_INFO' as ThresholdTypeEnum,
        UpperboundWarning: 'UPPERBOUND_WARNING' as ThresholdTypeEnum,
        LowerboundWarning: 'LOWERBOUND_WARNING' as ThresholdTypeEnum
    };
}


