## BACK END

Go to the Back-end folder

```bash
  cd back
```

For launch and generate the jacoco code coverage:

```bash
  mvn clean test
```

## FRONT END
#### E2E
Go to the Front-end folder

```bash
  cd front
```

Install dependencies:

```bash
  npm install
```

Launch Front-end:

```bash
  npm run start
```

Launching e2e test:

```bash
  npm run e2e
```

Take a browser like google and select the test all.cy.ts
Generate coverage report
```bash
  npm run e2e:coverage
```

#### Unitary test
Launching test:

```bash
  npm run test
```

for coverage:

```bash
  npm test -- --coverage
```
