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
import { Measurement } from '../model/measurement';
import { SensorId } from '../model/sensorId';
import { TemperaStation } from '../model/temperaStation';


export interface Sensor {
    sensorId?: SensorId;
    temperaStation?: TemperaStation;
    measurements?: Array<Measurement>;
    sensorType?: Sensor.SensorTypeEnum;
    unit?: Sensor.UnitEnum;
    id?: SensorId;
}
export namespace Sensor {
    export type SensorTypeEnum = 'TEMPERATURE' | 'IRRADIANCE' | 'HUMIDITY' | 'NMVOC';
    export const SensorTypeEnum = {
        Temperature: 'TEMPERATURE' as SensorTypeEnum,
        Irradiance: 'IRRADIANCE' as SensorTypeEnum,
        Humidity: 'HUMIDITY' as SensorTypeEnum,
        Nmvoc: 'NMVOC' as SensorTypeEnum
    };
    export type UnitEnum = 'CELSIUS' | 'FAHRENHEIT' | 'PERCENT' | 'LUX' | 'OHM';
    export const UnitEnum = {
        Celsius: 'CELSIUS' as UnitEnum,
        Fahrenheit: 'FAHRENHEIT' as UnitEnum,
        Percent: 'PERCENT' as UnitEnum,
        Lux: 'LUX' as UnitEnum,
        Ohm: 'OHM' as UnitEnum
    };
}


