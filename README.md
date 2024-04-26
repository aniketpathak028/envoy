<div align='center'>
<img width="300" height="300" alt="icon" src=".\assets\envoy512.png">
</div>
<br>

<div align="center">

<img alt="GitHub" src="https://img.shields.io/github/license/aniketpathak028/envoy?style=plastic">

<img alt="GitHub release (latest by date including pre-releases" src="https://img.shields.io/github/v/release/aniketpathak028/envoy?include_prereleases">

<img alt="GitHub top language" src="https://img.shields.io/github/languages/top/aniketpathak028/envoy?style=plastic">

<img alt="GitHub Repo forks" src="https://img.shields.io/github/forks/aniketpathak028/envoy?style=plastic">

<img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/aniketpathak028/envoy?style=plastic">

<img alt="GitHub Repo stars" src="https://img.shields.io/github/contributors-anon/aniketpathak028/envoy">
</div>

# Envoy

Welcome to Envoy - your all-in-one email management solution to schedule and track emails! 💌

## API

#### Base URL

```http
  https://envoy-api.up.railway.app
```

#### Send a Normal Email

```http
  POST /api/v1/send
```

| Parameter    | Type     | Description                                               |
|:-------------|:---------|:----------------------------------------------------------|
| `to`         | `email`  | **Required**. Recipient's email address                   |
| `subject`    | `string` | **Required**. Subject                                     |
| `body`       | `string` | **Required**. Body                                        |
| `cc`         | `array`  | **Optional**. Cc list                                     |
| `bcc`        | `array`  | **Optional**. Bcc list                                    |
| `trackEmail` | `email`  | **Optional**. Email address to recieve track notification |

#### Example Request

```json
{
  "to": "recipient@example.com",
  "subject": "Your Subject Here",
  "body": "Your Email Body Here",
  "cc": ["cc1@example.com", "cc2@example.com"],
  "bcc": ["bcc1@example.com", "bcc2@example.com"],
  "trackEmail": "notification@example.com"
}
```

#### Response

```json
{
    "success": true,
    "message": "Email sent successfully"
}
```

#### Schedule an Email

```http
  POST api/v1/schedule
```

| Parameter    | Type     | Description                                               |
|:-------------|:---------|:----------------------------------------------------------|
| `to`         | `email`  | **Required**. Recipient's email address                   |
| `subject`    | `string` | **Required**. Subject                                     |
| `body`       | `string` | **Required**. Body                                        |
| `dateTime`   | `time`   | **Required**. Scheduled time                              |
| `timeZone`   | `string` | **Required**. Sender's zone id                            |
| `cc`         | `array`  | **Optional**. Cc list                                     |
| `bcc`        | `array`  | **Optional**. Bcc list                                    |
| `trackEmail` | `email`  | **Optional**. Email address to recieve track notification |

#### Example Request

```json
{
    "to": "recipient@example.com",
    "subject": "Meeting Reminder",
    "body": "Don't forget our meeting tomorrow!",
    "dateTime": "2024-04-27T10:00:00Z",
    "zoneId": "America/New_York",
    "cc": ["cc1@example.com", "cc2@example.com"],
    "bcc": ["bcc@example.com"],
    "trackEmail": "track@example.com"
}
```

#### Response

```json
{
  "success": true,
  "jobId": "96db0d1b-608c-4cba-8f76-19924e90b3ba",
  "jobGroup": "email-jobs",
  "message": "Email Scheduled Successfully!"
}
```


## Project Created & Maintained By

### Aniket Pathak

<a href="https://www.linkedin.com/in/aniket-pathak-8925311b5/"><img src="https://github.com/aritraroy/social-icons/blob/master/linkedin-icon.png?raw=true" width="60"></a> <a href="https://twitter.com/AniketP51335534"><img src="https://github.com/aritraroy/social-icons/blob/master/twitter-icon.png?raw=true" width="60"></a>

[![GitHub followers](https://img.shields.io/github/followers/aniketpathak028.svg?style=social&label=Follow)](https://github.com/aniketpathak028/)

### Soham Dutta

<a href="https://www.linkedin.com/in/shm-dtt/"><img src="https://github.com/aritraroy/social-icons/blob/master/linkedin-icon.png?raw=true" width="60"></a> <a href="https://twitter.com/shmdsgn"><img src="https://github.com/aritraroy/social-icons/blob/master/twitter-icon.png?raw=true" width="60"></a>

[![GitHub followers](https://img.shields.io/github/followers/shm-dsgn.svg?style=social&label=Follow)](https://github.com/shm-dsgn/)

## Stargazers

[![Stargazers repo roster for @aniketpathak028/envoy](https://reporoster.com/stars/dark/aniketpathak028/envoy)](https://github.com/aniketpathak028/envoy/stargazers)

## Forkers

[![Forkers repo roster for @aniketpathak028/envoy](https://reporoster.com/forks/dark/aniketpathak028/envoy)](https://github.com/aniketpathak028/envoy/network/members)

<!-- License -->
## Copyright & License

Code and documentation Copyright (c) [Apache-2.0](LICENSE) © 2024 Envoy.