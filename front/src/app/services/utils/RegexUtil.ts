export const COMMON_NAME_TOOLTIP = "COMMON NAME (CN)\n" +
  "\n" +
  "Valid Examples\n" +
  "_____________\n" +
  "example.com\n" +
  "www.example.com\n" +
  "my-server.local\n" +
  "my-app-123\n" +
  "\n" +
  "Invalid Examples\n" +
  "_______________\n" +
  "$%&/example.com (contains special characters)\n" +
  ".example.com (starts with a dot)\n" +
  "example..com (double dot)";
export const COMMON_NAME_REGEX: RegExp = /^[\w-]+(?:\.[\w-]+)*$/;


export const ORGANIZATION_UNIT_TOOLTIP = "ORGANIZATION UNIT(OU)\n" +
  "\n" +
  "Valid Examples\n" +
  "______________\n" +
  "IT department\n" +
  "Marketing Sales\n" +
  "HR-Recruiting\n" +
  "Product Development\n" +
  "Invalid Organization Units:\n" +
  "\n" +
  "Invalid Examples\n" +
  "________________\n" +
  "$%&/IT department (contains special characters)\n" +
  "IT department! (contains an exclamation mark)\n" +
  "IT department (starts with whitespace)";
export const ORGANIZATION_UNIT_REGEX: RegExp = /^[\w-]+(?:\s+[\w-]+)*$/;



export const ORGANIZATION_NAME_TOOLTIP = "ORGANIZATION NAME(O)\n" +
  "\n" +
  "Valid Examples\n" +
  "_______________\n" +
  "Allows one or more words or spaces.\n" +
  "Does not allow special characters.\n" +
  "\n" +
  "Example Inc.\n" +
  "My Company Ltd.\n" +
  "XYZ Corp\n" +
  "\n" +
  "Invalid Examples\n" +
  "________________\n" +
  "$%&/Example Inc. (contains special characters)\n" +
  "Example Inc.! (contains an exclamation mark)";
export const ORGANIZATION_NAME_REGEX: RegExp = /^[\w\s]+\.?$/;



export const LOCALITY_NAME_TOOLTIP = "LOCALITY NAME(N)\n" +
  "\n" +
  "Valid Examples\n" +
  "_______________\n" +
  "San Francisco\n" +
  "New York City\n" +
  "London\n" +
  "\n" +
  "Invalid Examples\n" +
  "_________________\n" +
  "$%&/San Francisco (contains special characters)\n" +
  "San Francisco! (contains an exclamation mark)";
export const LOCALITY_NAME_REGEX: RegExp = /^[\w\s]+$/;


export const STATE_NAME_TOOLTIP = "STATE NAME(ST)\n" +
  "\n" +
  "Valid Examples\n" +
  "_____________ \n" +
  "California\n" +
  "New York\n" +
  "Ontario\n" +
  "\n" +
  "Invalid Examples\n" +
  "________________\n" +
  "$%&/California (contains special characters)\n" +
  "California! (contains an exclamation mark)"
export const STATE_NAME_REGEX: RegExp = /^[\w\s]+$/;



export const COUNTRY_NAME_TOOLTIP = "Country(C)\n" +
  "\n" +
  "Valid Examples\n" +
  "______________\n" +
  "US (United States)\n" +
  "CA (Canada)\n" +
  "GB (United Kingdom)\n" +
  "\n" +
  "Invalid Examples\n" +
  "________________\n" +
  "USA (not exactly two letters)\n" +
  "U.S. (contains a period)\n" +
  "us (not uppercase letters)";
export const COUNTRY_NAME_REGEX: RegExp = /^[A-Z]{2}$/;

/*
* Password has at least 8 and maximum 256 chars
* 1 uppercase, 1 lowercase, 1 number and 1 special characters are required
* **/
export const PASSWORD_REGEX: RegExp = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[^a-zA-Z0-9]).{8,256}$/;
