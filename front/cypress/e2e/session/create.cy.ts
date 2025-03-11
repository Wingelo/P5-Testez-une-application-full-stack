describe('Session create e2e tests', () => {
  const teachers =[
    {
      id:1,
      lastName: 'Doe',
      firstName: 'John',
      createdAt: new Date(),
      updatedAt: new Date(),
    },
    {
      id:2,
      lastName: 'Doe',
      firstName: 'Jane',
      createdAt: new Date(),
      updatedAt: new Date(),
    }
  ]
  //LOGIN
  it('Create a session', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: 1
      },
    })

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    // CREATE

    cy.intercept('GET', '/api/teacher', {
      body: teachers
    })

    cy.intercept('POST', '/api/session', {
      statusCode:200,
    })

    cy.intercept('GET', '/api/session', {
          id:1,
          name: 'A session Name',
          date: new Date(),
          teacher_id:1,
          description:'A small description',
          createdAt: new Date(),
          updatedAt: new Date()
    })

    cy.get('button[routerLink=create]').click()

    cy.url().should('include', '/sessions/create')

    cy.get('input[formControlName=name]').type('A session Name')
    cy.get('input[formControlName=date]').type('2024-02-02')
    cy.get('mat-select[formControlName=teacher_id]').click().then(() => {
      cy.get(`.cdk-overlay-container .mat-select-panel .mat-option-text`).should('contain', teachers[0].firstName);
      cy.get(`.cdk-overlay-container .mat-select-panel .mat-option-text:contains(${teachers[0].firstName})`).first().click().then(() => {
        cy.get(`[formcontrolname=teacher_id]`).contains(teachers[0].firstName);})
    })
    cy.get('textarea[formControlName=description]').type('A small description')

    cy.get('button[type=submit]').click()
    cy.url().should('include', '/sessions')
  })
})
