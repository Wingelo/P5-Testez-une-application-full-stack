describe('User e2e me test', () => {
  it('me', () => {
    // LOGIN
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    // INTERCEPT USER
    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        username: 'JohnDoe',
        firstName: 'John',
        lastName: 'Doe',
        email:'yoga@studio.com',
        admin: false,
        password: 'password',
        createdAt: new Date(),
        updatedAt: new Date(),
      },
    }).as('user')

    // INTERCEPT SESSION
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'userName',
        date: new Date(),
        teacher_id:1,
        description:'A small description',
        createdAt: new Date(),
        updatedAt: new Date(),
      }
    }).as('session')

    cy.get('span[routerLink=me]').click().then(()=>{
      cy.url().should('include', '/me').then(()=>{

        cy.get('p').contains("Name: John "+("Doe").toUpperCase())
        cy.get('p').contains("Email: yoga@studio.com")
      })
    })
    cy.get('button').first().click()
    // Vérifie que l'utilisateur est redirigé vers la page des sessions
    cy.url().should('include', '/sessions')
  })

});
